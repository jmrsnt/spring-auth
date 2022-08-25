package com.ds.auth.components;

import com.ds.auth.entities.User;
import com.ds.auth.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SubjectAttributeUserTokenConverter extends DefaultUserAuthenticationConverter {

    private final String userClaimName = JwtClaimNames.SUB;

    UserService userService;
    KeyPair keyPair;

    public SubjectAttributeUserTokenConverter(UserService userService, KeyPair keyPair) {
        this.userService = userService;
        this.keyPair = keyPair;
    }

    @PostConstruct
    void postConstruct() {
        this.setUserClaimName(userClaimName);
    }

    @Override
    public Map<String, Object> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        User user = userService.getUserByUsername(username);

        response.put(userClaimName, username);

        if (authorities != null && !authorities.isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authorities));
        }

        response.put("role", user.getRole().getName());

        return response;
    }

}
