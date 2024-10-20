package com.service.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.service.model.User;
import com.service.service.service.UserService;
import java.util.List;

import java.util.List;

@RestController
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String sayHello() {
        // Hardcoded user data
        User user = new User();
        user.setName("so3ad Doe");
        user.setEmail("john.doe@examlev.com");

        // Save the user to the database
        userService.saveUser(user);
        return "HelloWorldController is working";
    }
    @GetMapping
    public List<User> getAllUsers() {
        // Retrieve all users from the database
        return userService.getAllUsers();
    }

}
