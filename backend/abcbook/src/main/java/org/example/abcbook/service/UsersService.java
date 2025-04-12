package org.example.abcbook.service;

import org.example.abcbook.dto.request.UserRequest;
import org.example.abcbook.exception.AppException;
import org.example.abcbook.model.Users;

public interface UsersService {
    Users getUserById(Long id);

    void createUser(UserRequest userData) throws AppException, Exception;
}
