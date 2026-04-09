package com.library.service;

import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Tìm theo username (sẽ là SĐT của member)
        com.library.model.User user = userRepository
            .findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("Không tìm thấy tài khoản: " + username)
            );

        return User.withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())          // "MEMBER" hoặc "LIBRARIAN"
            .disabled(!user.isEnabled())
            .build();
    }
}
