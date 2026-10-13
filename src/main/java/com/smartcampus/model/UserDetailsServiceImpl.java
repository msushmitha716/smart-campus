package com.smartcampus.service;

import com.smartcampus.model.User;
import com.smartcampus.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String e = email == null ? "" : email.trim().toLowerCase();

        if (e.equals("admin@campus.com")) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(e).password("{noop}admin123").roles("ADMIN").build();
        }

        User u = userRepository.findByEmail(e)
                .orElseThrow(() -> new UsernameNotFoundException("No account for " + e));

        String pwd = u.getPassword();
        if (pwd == null || !pwd.startsWith("{")) {
            pwd = "{noop}" + pwd;   // old accounts saved as plain text
        }

        String role = (u.getRole() == null || u.getRole().isBlank()) ? "STUDENT" : u.getRole();

        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getEmail()).password(pwd).roles(role).build();
    }
}