package com.ds.auth.services;

import com.ds.auth.entities.User;
import com.ds.auth.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username != null) {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("Username " + username + " not found");
            } else {
                return user.get();
            }
        }
        throw new UsernameNotFoundException("Username not be blank.");
    }

    public User getUserByUsername(String username) {
        if (username != null) {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username " + username + " not found"
                );
            } else {
                return user.get();
            }
        }
        throw new UsernameNotFoundException("Username not be blank.");
    }

}
