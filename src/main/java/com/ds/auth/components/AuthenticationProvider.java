package com.ds.auth.components;

import com.ds.auth.entities.User;
import com.ds.auth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    UserService userService;

    public AuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param auth the authentication request object.
     */
    @Override
    public Authentication authenticate(Authentication auth) {
        if (auth == null) {
            throw new BadCredentialsException("Bad Credentials");
        }

        String username = auth.getName();
        String password = (String) auth.getCredentials();

        try {
            User user = userService.getUserByUsername(username);

            // @TODO Implementar Autenticação

            return new UsernamePasswordAuthenticationToken(user.getUsername(), password, user.getAuthorities());
        } catch (Exception ex) {
            logger.error("Ocorreu um erro ao realizar a autenticação.", ex);
        }

        throw new BadCredentialsException("External system authentication failed");
    }

    @Override
    public boolean supports(Class<?> auth) {
        if (auth != null) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(auth);
        } else {
            return false;
        }
    }

}
