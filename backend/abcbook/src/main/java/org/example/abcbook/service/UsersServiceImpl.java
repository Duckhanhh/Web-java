package org.example.abcbook.service;

import org.example.abcbook.dto.request.UserRequest;
import org.example.abcbook.exception.AppExeption;
import org.example.abcbook.exception.ErrorCode;
import org.example.abcbook.mapper.UsersMapper;
import org.example.abcbook.model.Users;
import org.example.abcbook.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    @Override
    public void createUser(UserRequest userData) {
        if (usersRepository.existsByEmail(userData.getEmail())) {
            throw new AppExeption(ErrorCode.EMAIL_EXISTED);
        }
        Users user = usersMapper.userRequestToUser(userData);
        user.setRegistrationDate(new Date());
        usersRepository.save(user);
    }
}
