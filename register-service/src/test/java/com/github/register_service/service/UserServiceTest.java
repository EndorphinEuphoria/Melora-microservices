package com.github.register_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.register_service.model.Rol;
import com.github.register_service.model.User;
import com.github.register_service.repository.RolRepository;
import com.github.register_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test 
    void findAll_returnsListFromRepository(){
        List<User> users = Arrays.asList(
            new User(1L, "test", "test@a.cl", "test", null, null),
            new User(2L, "test2", "test2@a.cl", "test2", null, null)
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertThat(result).isEqualTo(users);
    }

    @Test
    void findById_returnsUserByID(){
        User user = new User(1L, "test1", "test1@a.cl", "test1", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void findByEmail_returnsUserByEmail(){
        User user = new User(1L, "test1", "test1@a.cl", "test1", null, null);

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getByMail("test@gmail.com");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void deleteById_itsInvoked(){
        Long idUsuario = 1L;
        
        User user = new User(1L, "test1",  "test1@a.cl", "test1", null, null);
        
        when(userRepository.findById(idUsuario)).thenReturn(Optional.of(user));

        userService.deleteUserById(idUsuario);

        verify(userRepository).delete(user);
    }

    @Test
    void saveUser_savesUsers(){
        Rol rol = new Rol(1L, "a", null);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(passwordEncoder.encode("test1")).thenReturn("encrypt");

        User inputUser = new User(null, "test",  "test@a.cl", "test1", null, rol);

        User savedUser = new User(1L, "test",  "test@a.cl", "encrypt", null, rol);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.saveUser(inputUser, null);

        verify(passwordEncoder).encode("test1");

        assertThat(result.getEmail()).isEqualTo("test@a.cl");
        assertThat(result.getPassword()).isEqualTo("encrypt");
    }

    @Test
    public void testUpdateUserById() {

        User user = new User(1L, "test1", "test1@a.cl", "test1", null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User newUser = new User(1L, "test1",  "test2@a.cl", "test1", null, null);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.updateUserById(newUser, null);

        verify(userRepository).save(any(User.class));

        assertEquals("test2@a.cl", result.getEmail());
    }

}
