package com.example.link.reduce.service;

import com.example.link.reduce.data.repository.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserDetail implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        val user = userRepo.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found by login: "+ login));

        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("USER"));
        return new org.springframework.security.core.userdetails.User(login, user.getPassword(), authorities);
    }
}
