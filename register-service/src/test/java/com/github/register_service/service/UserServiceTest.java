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

    //Aqui verificamos que al momento de encontrar todos los usuarios el metodo alojado en el servicio se comporte de igual manera como lo haria el repositorio
    @Test 
    void findAll_returnsListFromRepository(){
        List<User> users = Arrays.asList(
            new User(1L, "test", "test", "test", "test@a.cl", "test", "test", null),
            new User(2L, "test2", "test2", "test2", "test2@a.cl", "test2",  "test2", null));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertThat(result).isEqualTo(users);

    }

    //Aqui verificamos que al momento de encontrar todos los usuarios el metodo alojado en el servicio se comporte de igual manera como lo haria el repositorio
    @Test
    void findById_returnsUserByID(){
        User user = new User(1L, "test1", "test1", "test1", "test1@a.cl", "test1", "test1", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertThat(result).isEqualTo(user);
    }

    //Aqui verificamos que al momento de encontrar  los usuarios por el email el metodo alojado en el servicio se comporte de igual manera como lo haria el repositorio
    @Test
    void findByEmail_returnsUserByEmail(){
        User user = new User(1L, "test1", "test1", "test1", "test1@a.cl", "test1", "test1", null);

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getByMail("test@gmail.com");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    
    //Aqui verificamos que al momento de encontrar  los usuarios por el email el metodo alojado en el servicio se comporte de igual manera como lo haria el repositorio
    @Test
    void deleteById_itsInvoked(){
        Long idUsuario = 1L;
        
        User user = new User(1L, "test1", "test1", "test1", "test1@a.cl", "test1", "test1", null);
        
        when(userRepository.findById(idUsuario)).thenReturn(Optional.of(user));

        userService.deleteUserById(idUsuario);

        verify(userRepository).delete(user);
    }

    //aqui verificamos que al guarde el usuario de manera correcta
    @Test
    void saveUser_savesUsers(){
    Rol rol = new Rol(1L, "a", null);
    
    when(rolRepository.findById(rol.getIdRol())).thenReturn(Optional.of(rol));

    User user = new User(1L, "test1", "test1", "test1", "test1@a.cl", "test1", "test1", rol);

    when(passwordEncoder.encode("test1")).thenReturn("encrypt");

    User savedUser = new User(1L, "test@gmail.com", "test1", "test1", "test1@a.cl", "encrypt", "test1", null);

    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    User result = userService.saveUser(user);

    verify(passwordEncoder).encode("test1");

    assertThat(result.getEmail()).isEqualTo("test1@a.cl");
    assertThat(result.getPassword()).isEqualTo("encrypt");
}

    @Test
    public void testUpdateUserById() {

    User user = new User(1L, "test1", "test1", "test1", "test1@a.cl", "test1", "test1", null);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    User newUser = new User(1L, "test1", "test1", "test1", "test2@a.cl", "test1", "test1", null);
    
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    User result = userService.updateUserById(newUser);
    verify(userRepository).save(any(User.class));

    assertEquals("test2@a.cl", result.getEmail());
  

}
}
