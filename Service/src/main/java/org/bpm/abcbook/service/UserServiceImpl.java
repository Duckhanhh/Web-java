package org.bpm.abcbook.service;

import org.bpm.abcbook.model.User;
import org.bpm.abcbook.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public List<User> findAllUsers() throws Exception {
        return userRepo.findAll();
    }
}
