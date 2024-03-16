package com.example.usermicroservice.security;


import com.example.usermicroservice.entity.User;
import com.example.usermicroservice.service.UserServiceImpl;
import com.example.usermicroservice.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetaisService implements UserDetailsService {
    private User user;
    @Autowired
    private UserServiceImpl userService;
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username) );
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(SecurityUtils.convertToAuthority(user.getRole().name()));

        return UserPrincipal.builder()
                .user(user)
                .id(user.getId())
                .email(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

}
