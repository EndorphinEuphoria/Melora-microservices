package com.github.register_service.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.register_service.dto.UserDto;
import com.github.register_service.model.User;
import com.github.register_service.model.UserDetail;
import com.github.register_service.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDto dto = new UserDto();
        dto.setId(user.getIdUser());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRolId(user.getRol().getIdRol());

        return new UserDetail(dto);
    }

}