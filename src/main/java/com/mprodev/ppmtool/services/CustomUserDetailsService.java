package com.mprodev.ppmtool.services;

import com.mprodev.ppmtool.domain.User;
import com.mprodev.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public User loadUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }
}
