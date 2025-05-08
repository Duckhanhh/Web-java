package org.example.abcbook.service;

import org.example.abcbook.dto.request.UserRequest;

import org.example.abcbook.dto.response.UserResponse;
import org.example.abcbook.exception.AppException;
import org.example.abcbook.mapper.UsersMapper;
import org.example.abcbook.model.Users;
import org.example.abcbook.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersRepo usersRepository;
    @Autowired
    private UsersMapper usersMapper;
    
    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return usersRepository.findAll().stream().map(usersMapper::toUserResponse).toList();
    }

    public void createUser(UserRequest userData) throws AppException {
        if (userData == null) {
            throw new AppException("CU00001", "create.user.data.empty");
        }

        if (usersRepository.existsByEmail(userData.getEmail())) {
            throw new AppException("CU00002", "create.user.email.existed");
        }

        if (usersRepository.existsByPhoneNumber(userData.getPhoneNumber())) {
            throw new AppException("CU00003", "create.user.phoneNumber.existed");
        }

        //Encode password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(userData.getPassword());
        userData.setPassword(encodedPassword);

        Users user = usersMapper.userRequestToUser(userData);
        user.setRegistrationDate(new Date());
        usersRepository.save(user);
    }
}
