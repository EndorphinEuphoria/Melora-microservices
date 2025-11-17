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


    public boolean validateLogin(String email, String rawPassword) {

    Optional<User> usuarioOpt = userRepository.findByEmail(email);

    if (usuarioOpt.isEmpty()) {
        return false; 
    }

    User usuario = usuarioOpt.get();

    return passwordEncoder.matches(rawPassword, usuario.getPassword());
}


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
    return userRepository.findByEmail(email);
}


    public void deleteUserById(Long idUsuario) {
        User exist = userRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Usuario a eliminar no existe."));
        userRepository.delete(exist);
    }

   public User saveUser(User user, String base64Photo) {
    User userToSave = new User();

    Rol rol;
    if (user.getRol() == null || user.getRol().getIdRol() == null) {
        rol = rolRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Rol con ID 1 no encontrado."));
    } else {
        rol = findRol(user.getRol().getIdRol());
    }
    
    userToSave.setNickname(user.getNickname());
    userToSave.setEmail(user.getEmail());
    userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
    userToSave.setRol(rol);

    if (base64Photo != null && !base64Photo.isBlank()) {
        userToSave.setProfilePhotoUrl(decodeBase64(base64Photo));
    }

    return userRepository.save(userToSave);
}

    public User findUserById(Long idUser) {
        return userRepository.findById(idUser)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    }

    public User updateUserById(User user, String base64Photo) {
        User newUserInfo = userRepository.findById(user.getIdUser()).orElseThrow(() -> new EntityNotFoundException("Usuario inexistente."));


        if (user.getNickname() != null && !user.getNickname().trim().isBlank()) {
            newUserInfo.setNickname(user.getNickname());
        }

        if (user.getEmail() != null && !user.getEmail().trim().isBlank()) {
            newUserInfo.setEmail(user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().trim().isBlank()) {
            newUserInfo.setPassword(encrypt(user.getPassword()));
        }

        if (user.getRol() != null) {
            newUserInfo.setRol(user.getRol());
        }
        
         if (base64Photo != null && !base64Photo.isBlank()) {
        newUserInfo.setProfilePhotoUrl(decodeBase64(base64Photo));
    }

        return userRepository.save(newUserInfo);
    }

    private byte[] decodeBase64(String Base64){
        return java.util.Base64.getDecoder().decode(Base64);
    }

}