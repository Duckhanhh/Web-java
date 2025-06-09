package org.bpm.abcbook.service;

import org.bpm.abcbook.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers() throws Exception;
}
