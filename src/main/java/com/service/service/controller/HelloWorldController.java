package com.service.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.service.model.User;
import com.service.service.service.UserService;

@RestController
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String sayHello() {
        // Hardcoded user data
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@examplev.com");

        // Save the user to the database
        userService.saveUser(user);
        return "turkish hallo";
    }
}
