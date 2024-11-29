package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                               @JsonView(UserRecordDTO.UserView.RegistrationPost.class) UserRecordDTO userRecordDTO){

        if(userService.existsByUsername(userRecordDTO.username())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username already exists");
        }
        if(userService.existsByEmail(userRecordDTO.email())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDTO));
    }
}
