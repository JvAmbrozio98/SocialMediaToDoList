package com.example.SocialMediaToDoList.Controllers;

import com.example.SocialMediaToDoList.DTO.UserDTO;
import com.example.SocialMediaToDoList.Execptions.UserNotFound;
import com.example.SocialMediaToDoList.Models.User.User;
import com.example.SocialMediaToDoList.Repository.UserRepository;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    private UserRepository userRepository;



    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration)  throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean

    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder() ;
    }


    public ModelMapper modelMapper () {
        return new ModelMapper();
    }

    @GetMapping(path = "users",headers = "UserAcessLevel=USER")
    public List<UserDTO> getAllUsers() {
        List<User> listEntityUsers = userRepository.findAll();
        ModelMapper mapper = new ModelMapper();
        return mapper.map(listEntityUsers,new TypeToken<List<UserDTO>>(){}.getType());
    }

    @GetMapping(path = "users",headers = "UserAcessLevel=ADMIN")
    public List<User> getAllUsersAdmin() {
        return userRepository.findAll();
    }

    @GetMapping(value = "users/{id}",headers = "UserAcessLevel=USER")
    public EntityModel<UserDTO> getUserById(@PathVariable Long id) throws Exception {

        if(userRepository.existsById(id)) {
           User foundedUser = userRepository.findById(id).get();
            UserDTO userdto = modelMapper().map(foundedUser,UserDTO.class);
            EntityModel<UserDTO> UserDtoEntity = EntityModel.of(userdto);
            WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
            UserDtoEntity.add(link.withRel("All-Users:"));
            return UserDtoEntity;


        }
        throw new UserNotFound("Not found on the database");
    }

    @GetMapping(value = "users/{id}",headers = "UserAcessLevel=DEV")
    public MappingJacksonValue getUserByIdJSONFormat(@PathVariable Long id) throws Exception {

        if(userRepository.existsById(id)) {
            User foundedUser = userRepository.findById(id).get();
            UserDTO userdto = modelMapper().map(foundedUser,UserDTO.class);
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userdto);
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
            FilterProvider filters = new SimpleFilterProvider().addFilter("DTOFilter",filter);
            mappingJacksonValue.setFilters(filters);

            return mappingJacksonValue;


        }
        throw new UserNotFound("Not found on the database");
    }




    @PostMapping
    public ResponseEntity<User> createUser (@RequestBody User user) {
        User newUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
      return ResponseEntity.created(location).build();
    }


    @PostMapping
    public ResponseEntity registerUser (@RequestBody @Valid User user) throws Exception {
        if(this.userRepository.findById(user.getId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        String encrypetedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        User newUser = new User(user.getLogin(),user.getTypeOfUser(),encrypetedPassword);
        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public User updateUser (@PathVariable Long id,@RequestBody User user) throws Exception {

        if(userRepository.existsById(id)) {
            User existingUser = userRepository.findById(id).get();
            existingUser.setName(user.getName());
            existingUser.setDate(user.getDate());
            return userRepository.save(existingUser);
        }

        throw  new UserNotFound("User not found on the database ");
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            System.out.println("Deleting user with ID: " + id);
            userRepository.deleteById(id);
            return "User deleted";
        } else {
            return "User not found";
        }
    }

}



