package org.example.abcbook.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.example.abcbook.dto.request.AuthenticationRequest;
import org.example.abcbook.dto.request.IntrospectRequest;
import org.example.abcbook.dto.response.AuthenticationResponse;
import org.example.abcbook.dto.response.IntrospectResponse;
import org.example.abcbook.exception.AppException;
import org.example.abcbook.model.Users;
import org.example.abcbook.repository.UsersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private UsersRepo usersRepository;

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
                .token(generateToken(user))
                .build();
    }

    private String generateToken(Users users) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(users.getEmail())
                .issuer("abcbook")
                .claim("scope", builderScope(users))
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jsonObject = new JWSObject(header, payload);
        try {
            jsonObject.sign(new MACSigner(signerKey.getBytes()));
            return jsonObject.serialize();
        } catch (JOSEException e) {
            logger.error(e.getMessage());
            throw new AppException("L00004", "common.login.sign.failed");
        }
    }

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws AppException, Exception {
        var token = introspectRequest.getToken();
        //verify token
        JWSVerifier verifier = new MACVerifier(signerKey);
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
                .build();
    }

    private String builderScope(Users users) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!users.getRole().isEmpty() || !users.getRole().isBlank()) {
            stringJoiner.add("ROLE_" + users.getRole());
        }
        return stringJoiner.toString();
    }
}
