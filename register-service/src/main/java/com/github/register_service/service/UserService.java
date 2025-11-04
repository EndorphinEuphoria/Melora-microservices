package com.github.register_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.register_service.model.Rol;
import com.github.register_service.model.User;
import com.github.register_service.repository.RolRepository;
import com.github.register_service.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    private String encrypt(String password) {
        return passwordEncoder.encode(password);
    }

    public Rol findRol(Long idRol) {
        return rolRepository.findById(idRol).orElseThrow(() -> new EntityNotFoundException("Rol no encontrado."));
    }

    public List<User> findAllUsers() {
        List<User> exist = userRepository.findAll();
        if (exist.isEmpty()) {
            throw new EntityNotFoundException("No existen usuarios");
        }
        return exist;
    }

    public Optional<User> getByMail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Usuario no existe.");
        }
        return user;
    }

    public void deleteUserById(Long idUsuario) {
        User exist = userRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Usuario a eliminar no existe."));
        userRepository.delete(exist);
    }

    public User saveUser(User user) {
        User userToSave = new User();
        if (findRol(user.getRol().getIdRol()) == null) {
            throw new EntityNotFoundException("Rol no encontrado.");
        }

        userToSave.setName(user.getName());
        userToSave.setLName(user.getLName());
        userToSave.setNickname(user.getNickname());
        userToSave.setEmail(user.getEmail());
        userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
        userToSave.setRol(user.getRol());
        userToSave.setProfilePhotoUrl(user.getProfilePhotoUrl());
        return userRepository.save(userToSave);
        
    }

    public User findUserById(Long idUser) {
        return userRepository.findById(idUser)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    }

    public User updateUserById(User user) {
        User newUserInfo = userRepository.findById(user.getIdUser()).orElseThrow(() -> new EntityNotFoundException("Usuario inexistente."));
        if (user.getName() != null && !user.getName().trim().isBlank()) {
            newUserInfo.setName(user.getName());
        }

        if (user.getLName() != null && !user.getLName().trim().isBlank()) {
            newUserInfo.setLName(user.getLName());
        }

        if (user.getNickname() != null && !user.getNickname().trim().isBlank()) {
            newUserInfo.setNickname(user.getNickname());
        }

        if (user.getEmail() != null && !user.getEmail().trim().isBlank()) {
            newUserInfo.setEmail(user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().trim().isBlank()) {
            newUserInfo.setPassword(encrypt(user.getPassword()));
        }

        // TODO: CAMBIAR, NO CREO QUE FUNCIONE :(
        if (user.getProfilePhotoUrl() != null && !user.getProfilePhotoUrl().trim().isBlank()) {
            newUserInfo.setProfilePhotoUrl(user.getProfilePhotoUrl());
        }

        if (user.getRol() != null) {
            newUserInfo.setRol(user.getRol());
        }

        return userRepository.save(newUserInfo);
    }


}