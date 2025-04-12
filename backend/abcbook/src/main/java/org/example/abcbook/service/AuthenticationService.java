package org.example.abcbook.service;

import org.example.abcbook.dto.request.AuthenticationRequest;
import org.example.abcbook.dto.request.IntrospectRequest;
import org.example.abcbook.dto.response.AuthenticationResponse;
import org.example.abcbook.dto.response.IntrospectResponse;
import org.example.abcbook.exception.AppException;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws AppException, Exception;
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws AppException, Exception;
}
