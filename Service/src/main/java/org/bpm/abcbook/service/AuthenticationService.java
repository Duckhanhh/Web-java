package org.bpm.abcbook.service;

import org.bpm.abcbook.dto.request.AuthenticationRequest;
import org.bpm.abcbook.dto.request.IntrospectRequest;
import org.bpm.abcbook.dto.response.AuthenticationResponse;
import org.bpm.abcbook.dto.response.IntrospectResponse;
import org.bpm.abcbook.exception.AppException;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws AppException, Exception;

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws AppException, Exception;
}
