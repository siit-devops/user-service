package com.devops.user_service.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KeycloakRolesMapper {
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";


    public Collection<GrantedAuthority> mapAuthorities(Jwt jwt) {
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
        try {
            String rolesStr = jwt.getClaimAsMap(REALM_ACCESS_CLAIM).get(ROLES_CLAIM).toString();
            int firstIndexOfBracket = rolesStr.indexOf("[");
            int lastIndexOfBracket = rolesStr.lastIndexOf("]");
            String role = rolesStr.substring(firstIndexOfBracket + 1, lastIndexOfBracket);
            mappedAuthorities.add(new SimpleGrantedAuthority(role));

            return mappedAuthorities;
        } catch (NullPointerException e) {
            return new HashSet<>();
        }
    }
}
