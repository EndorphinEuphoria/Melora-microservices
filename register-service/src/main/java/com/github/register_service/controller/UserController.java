package com.github.register_service.controller;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.register_service.dto.UserDto;
import com.github.register_service.model.Rol;
import com.github.register_service.model.User;
import com.github.register_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api-v1/auth")
@RequiredArgsConstructor
public class UserController {

    // TODO cambiar ResponseEntity<TYPE>
    private final UserService userService;

   @PostMapping("/add")
@Operation(summary = "Este endpoint permite crear usuarios.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "CREATED: indica que el usuario ha sido creado con éxito.",
                content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: indica que el correo ya existe.",
                content = @Content(schema = @Schema(implementation = String.class)))
})
public ResponseEntity<?> addUser(@RequestBody UserDto dto) {
    System.out.println("ROL RECIBIDO DESDE ANDROID = " + dto.getRolId());
    System.out.println("DTO COMPLETO = " + dto);

    try {
        if (userService.getByMail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Correo en uso, por favor utilice otro.");
        }

        User user = new User();
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        Rol rol = new Rol();
        rol.setIdRol(dto.getRolId() != null ? dto.getRolId() : 2L);
        user.setRol(rol);

        User newUser = userService.saveUser(user, dto.getProfilePhotoBase64());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("mensaje", "Usuario creado correctamente.");
        body.put("usuario", newUser);

        EntityModel<Map<String, Object>> resource = EntityModel.of(body);

        resource.add(linkTo(methodOn(UserController.class)
                .existsById(newUser.getIdUser())).withSelfRel());

        resource.add(linkTo(methodOn(UserController.class)
                .getAllUsers()).withRel("todos"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);

    } catch (DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Error: El correo o nickname ya está en uso.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + e.getMessage());
    }
}



    @Operation( summary = "Este endpoint permite comprobar la existencia de un usuario por email.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "OK: indica que el usuario ha sido encontrado con éxito.", content = @Content(schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "404", description = "NOT FOUND: indica que el usuario no ha sido encontrado.", content = @Content(schema = @Schema(implementation = User.class)))})
    @PostMapping("/getbymail")
    public ResponseEntity<?> getUserByMail(@RequestBody User user) {
        try {
            Optional<User> user1 = userService.getByMail(user.getEmail());
            if (!user1.isEmpty()) {
                EntityModel<User> userModel = EntityModel.of(user1.get());
                userModel.add(linkTo(methodOn(UserController.class).getUserByMail(user)).withSelfRel());
                userModel.add(linkTo(methodOn(UserController.class).existsById(user1.get().getIdUser())).withRel("ver-por-id"));
                userModel.add(linkTo(methodOn(UserController.class).deleteById(user1.get().getIdUser())).withRel("eliminar"));
                return ResponseEntity.status(HttpStatus.OK).body(userModel);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar." + e.getMessage());
        }
    }

    @Operation( summary = "Este endpoint permite comprobar la existencia de un usuario por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK: indica que el usuario ha sido encontrado con éxito.", content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND: indica que el usuario no ha sido encontrado.", content = @Content(schema = @Schema(implementation = User.class)))
    })
    @GetMapping("/exists/{idUser}")
    public ResponseEntity<?> existsById(@PathVariable Long idUser) {
        try {
            User user1 = userService.findUserById(idUser);
            EntityModel<User> userModel = EntityModel.of(user1);

            userModel.add(linkTo(methodOn(UserController.class).existsById(idUser)).withSelfRel());
            userModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("todos"));
            userModel.add(linkTo(methodOn(UserController.class).deleteById(idUser)).withRel("eliminar"));
            userModel.add(linkTo(methodOn(UserController.class)
        .updateUser(idUser, new UserDto())).withRel("actualizar"));

            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con ID: " + idUser + " no encontrado.");
        }
    }

    @Operation( summary = "Este endpoint permite borrar a un usuario por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK: indica que el usuario ha sido borrado con éxito", content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND: indica que el usuario no ha sido encontrado.", content = @Content(schema = @Schema(implementation = String.class)))
    })
   @DeleteMapping("/delete/{idUser}")
public ResponseEntity<?> deleteById(@PathVariable Long idUser) {
    try {
        userService.deleteUserById(idUser);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "El usuario ha sido borrado con éxito.");

        EntityModel<Map<String, Object>> resource = EntityModel.of(body);
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("todos"));

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

   @Operation( summary = "Este endpoint permite actualizar información de un usuario a través de su ID.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "OK: indica que la información del usuario se actualizó correctamente", content = @Content(schema = @Schema(implementation = User.class))),
    @ApiResponse(responseCode = "404", description = "NOT FOUND: indica que el usuario no ha sido encontrado.", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "409", description = "CONFLICT: indica que hubo un conflicto, el email no puede estar duplicado", content = @Content(schema = @Schema(implementation = String.class)))
})
@PatchMapping("/update/{idUser}")
public ResponseEntity<?> updateUser(
        @PathVariable Long idUser,
        @RequestBody UserDto dto) {

    try {
        // Crear objeto User parcial
        User user = new User();
        user.setIdUser(idUser);
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        // Rol
        if (dto.getRolId() != null) {
            Rol r = new Rol();
            r.setIdRol(dto.getRolId());
            user.setRol(r);
        }

        // Foto Base64 (puede ser null)
        String base64 = dto.getProfilePhotoBase64();

        // Actualizar usuario
        User updatedUser = userService.updateUserById(user, base64);

        // Respuesta HATEOAS
        EntityModel<User> userModel = EntityModel.of(updatedUser);
        userModel.add(linkTo(methodOn(UserController.class).updateUser(idUser, dto)).withSelfRel());
        userModel.add(linkTo(methodOn(UserController.class).existsById(idUser)).withRel("ver-usuario"));
        userModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("todos"));

        return ResponseEntity.status(HttpStatus.OK).body(userModel);

    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

    } catch (DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("El email ingresado ya está en uso");
    }
}


    @Operation( summary = "Este endpoint permite obtener una lista con todos los usuarios registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK: indica que los usuarios han sido encontrados con éxito.", content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND: indica que no hay usuarios registrados.", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/getall")
    public ResponseEntity<?> getAllUsers() {
        try{
            List<User> users = userService.findAllUsers();

            List<EntityModel<User>> usersModel = users.stream().map(user -> {
                EntityModel<User> model = EntityModel.of(user);
                model.add(linkTo(methodOn(UserController.class).existsById(user.getIdUser())).withSelfRel());
                model.add(linkTo(methodOn(UserController.class).deleteById(user.getIdUser())).withRel("eliminar"));
                model.add(linkTo(methodOn(UserController.class).updateUser(user.getIdUser(), new UserDto())).withRel("actualizar"));
                return model;
            }).toList();

            CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(usersModel, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

            return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen usuarios registrados.");
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> validateUsuario(@RequestBody User userRequest) {
    try {

        boolean isValid = userService.validateLogin(
            userRequest.getEmail(),
            userRequest.getPassword()
        );

        if (!isValid) {
            return ResponseEntity.badRequest().body("Email o contraseña incorrectas");
        }

        Optional<User> userOpt = userService.getByMail(userRequest.getEmail());
        User user = userOpt.get();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("self", "http://localhost:8082/api-v1/auth/login");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("idUser", user.getIdUser());
        data.put("email", user.getEmail());
        data.put("nickname", user.getNickname());
        data.put("rolId", user.getRol().getIdRol());
        data.put("rolName", user.getRol().getNombre());  
        data.put(
            "profilePhotoBase64",
            user.getProfilePhotoUrl() != null
                ? Base64.getEncoder().encodeToString(user.getProfilePhotoUrl())
                : null
        );

        body.put("data", data);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(body);

    } catch (Exception e) {
        throw new RuntimeException("Error al autenticar. " + e.getMessage());
    }
}



@GetMapping("/search")
public ResponseEntity<?> searchUsers(@RequestParam("nickname") String nickname) {

    try {
        List<User> users = userService.searchByNickname(nickname);

        List<Map<String, Object>> result = users.stream().map(user -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("idUser", user.getIdUser());
            map.put("nickname", user.getNickname());
            map.put("email", user.getEmail());
            map.put("profilePhotoBase64",
                user.getProfilePhotoUrl() != null
                    ? Base64.getEncoder().encodeToString(user.getProfilePhotoUrl())
                    : null
            );
            return map;
        }).toList();

        return ResponseEntity.ok(result);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar usuarios: " + e.getMessage());
    }
}

}
