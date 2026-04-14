package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.CustomUserDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {

    private CustomUserDetailsService userService;

    UserController(CustomUserDetailsService userService) {
        this.userService = userService;
    }



    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        User createdUser =  userService.createUser(user);
        

        ResponseEntity<User> response = ResponseEntity.ok(createdUser);

        return response;
    }
    

    
}
