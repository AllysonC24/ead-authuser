package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);
    final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserRecordDTO.UserView.RegistrationPost.class)
                                               @JsonView(UserRecordDTO.UserView.RegistrationPost.class) UserRecordDTO userRecordDTO){
        logger.debug("POST registerUser userRecordDTO {} ", userRecordDTO);
        if(userService.existsByUsername(userRecordDTO.username())){
            logger.warn("Username {} is Already Taken ", userRecordDTO.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username already exists");
        }
        if(userService.existsByEmail(userRecordDTO.email())){
            logger.warn("Email {} is Already Taken ", userRecordDTO.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDTO));
    }

    @GetMapping("/logs")
    public String index(){
        logger.trace("TRACE");
        logger.debug("DEBUG");
        logger.info("INFO");
        logger.warn("WARN");
        logger.error("ERROR");
        return "Logging Spring Boot...";
    }

/**
    @GetMapping("/logs")
    public String index(){
        log.trace("TRACE");
        log.debug("DEBUG");
        log.info("INFO");
        log.warn("WARN");
        log.error("ERROR");
        return "Logging Spring Boot...";
    }
**/
}
