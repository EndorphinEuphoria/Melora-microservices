package com.github.register_service.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.register_service.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetail implements UserDetails {

    private final UserDto userDto;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName= switch(userDto.getRolId().intValue()) {
            case 1 -> "ADMIN";
            case 2 -> "USER";
            default -> "UNKNOWN" ;
        };

        return List.of(new SimpleGrantedAuthority(roleName));
    }
    
    
    @Override
    public String getUsername() {
        return userDto.getEmail();
    }


    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

}
