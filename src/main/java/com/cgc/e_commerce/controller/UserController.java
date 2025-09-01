package com.cgc.e_commerce.controller;

import com.cgc.e_commerce.model.User;
import com.cgc.e_commerce.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) { this.userRepository = userRepository; }


    @GetMapping
    public List<User> list(){ return userRepository.findAll(); }
}
