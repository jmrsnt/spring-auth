package com.ds.auth.components;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.Serializable;

public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    UserDetailsService userDetailsService;

    public PermissionEvaluator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        return true;
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        throw new RuntimeException("Não implementado.");
    }

}
