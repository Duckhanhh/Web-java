package org.bpm.abcbook.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.bpm.abcbook.dto.request.AuthenticationRequest;
import org.bpm.abcbook.dto.request.IntrospectRequest;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.mapper.StaffMapper;
import org.bpm.abcbook.model.Staff;
import org.bpm.abcbook.repository.StaffRepo;
import org.bpm.abcbook.dto.response.AuthenticationResponse;
import org.bpm.abcbook.dto.response.IntrospectResponse;
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
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Value("${jwt.signer.key}")
    private String signerKey;

    @Autowired
    private StaffRepo staffRepo;

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws AppException, Exception {
        if (authenticationRequest == null
                || authenticationRequest.getPassword() == null
                || authenticationRequest.getEmail() == null) {
            throw new AppException("L00001", "Thiếu thông tin đăng nhập");
        }
        var staff = staffRepo.getByEmail(authenticationRequest.getEmail());

        if (staff == null) {
            throw new AppException("L00002", "Sai thông tin đăng nhập");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), staff.getPassword())) {
            throw new AppException("L00003", "Sai email hoặc mật khẩu");
        }

        var staffResponse = staffMapper.toStaffResponse(staff);
        return AuthenticationResponse.builder()
                .staffResponse(staffResponse)
                .authenticated(true)
                .token(generateToken(staff))
                .build();
    }

    private String generateToken(Staff users) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(users.getEmail())
                .issuer("abcbook")
                .claim("scope", builderScope(users))
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()))
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

    @Override
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

    private String builderScope(Staff users) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!users.getRole().isEmpty() || !users.getRole().isBlank()) {
            stringJoiner.add("ROLE_" + users.getRole());
        }
        return stringJoiner.toString();
    }
}
