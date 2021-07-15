package com.mprodev.ppmtool.services;

import com.mprodev.ppmtool.domain.User;
import com.mprodev.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.mprodev.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser) {
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        userRepository.findByUsername(newUser.getUsername())
                .ifPresent(user -> {
                    throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
                });
        return userRepository.save(newUser);
    }

}
