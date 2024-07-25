package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.request.UserRequest;
import com.project.musicapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Sign-up account
    @PostMapping("")
    public Response addUser(@RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest);
    }

    /*
        Handle get id from access token, not access id in param
     */
    @PutMapping("/{id}")
    public Response updateUser(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }
}
