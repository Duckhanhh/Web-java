package org.example.abcbook.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.example.abcbook.dto.request.AuthenticationRequest;
import org.example.abcbook.dto.request.IntrospectRequest;
import org.example.abcbook.dto.response.AuthenticationResponse;
import org.example.abcbook.dto.response.IntrospectResponse;
import org.example.abcbook.exception.AppException;
import org.example.abcbook.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @NonFinal
    protected static final String SIGNER_KEY = "1OerMfDKLdBNq/XOShuiHShj0NIUb3L/9oSFlX0P6AHELuL9hLK6hz3qJ/rWAa2T";

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws AppException, Exception {
        if (authenticationRequest == null
                || authenticationRequest.getPassword() == null
                || authenticationRequest.getEmail() == null) {
            throw new AppException("L00001", "common.login.data.empty");
        }
        var user = usersRepository.getByEmail(authenticationRequest.getEmail())
                .orElse(null);

        if (user == null) {
            throw new AppException("L00002", "common.login.user.not.exist");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new AppException("L00003", "common.login.email.or.password.invalid");
        }

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(generateToken(authenticationRequest.getEmail()))
                .build();
    }

    private String generateToken(String email) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("abcbook")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jsonObject = new JWSObject(header, payload);
        try {
            jsonObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jsonObject.serialize();
        } catch (JOSEException e) {
            logger.error(e.getMessage());
            throw new AppException("L00004", "common.login.sign.failed");
        }
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws AppException, Exception {
        var token = introspectRequest.getToken();
        //verify token
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);
        SignedJWT signedJWT = SignedJWT.parse(token);

        //Kiem tra token da het han hay chua
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!expirationDate.after(new Date())) {
            throw new AppException("I00002", "common.token.expired");
        }

        //Kiem tra token co dung khong
        var verify = signedJWT.verify(verifier);
        if (!verify) {
            throw new AppException("I00001", "common.token.invalid");
        }

        return IntrospectResponse.builder()
                .valid(true)
                .build() ;
    }
}
