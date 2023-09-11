package com.example.link.reduce.controller;

import com.example.link.reduce.controller.dto.LoginProperties;
import com.example.link.reduce.controller.dto.SignUpProperties;
import com.example.link.reduce.data.entity.UserEntity;
import com.example.link.reduce.service.IUserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    public UserController(IUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/in/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginProperties loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
    }

    @PostMapping("/in/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpProperties signUpProperties){
        // checking for username exists in a database
        if(userService.existsByUserLogin(signUpProperties.getLogin())){
            return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
        }
        // checking for email exists in a database
        if(userService.existsByUserEmail(signUpProperties.getEmail())){
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        // creating user object
        val user = new UserEntity();
        user.setLogin(signUpProperties.getLogin());
        user.setName(signUpProperties.getName());
        user.setEmail(signUpProperties.getEmail());
        user.setPassword(passwordEncoder.encode(signUpProperties.getPassword()));
        userService.saveUser(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }

    @GetMapping("/api/{id}")
    public Optional<UserEntity> getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/api/user")
    public Optional<UserEntity> getUserInfo() {
        return userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/api/logout")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            request.getSession().invalidate();
        }
        return new ResponseEntity<>("User logout  successfully!", HttpStatus.OK);
    }
}
