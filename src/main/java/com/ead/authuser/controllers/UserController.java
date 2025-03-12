package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "http://example.com", maxAge = 3600)
public class UserController {

    final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                       Pageable pageable,
                                                       @RequestParam(required = false) UUID courseId) {
        Page<UserModel> userModelPage = (courseId != null)
                ? userService.findAll(SpecificationTemplate.usersByCourseId(courseId).and(spec), pageable)
                : userService.findAll(spec, pageable);

        if(!userModelPage.isEmpty()){
            for(UserModel user : userModelPage.toList()){
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
        log.debug("GET getOneUser userId received {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        log.debug("DELETE deleteUser userId received {}", userId);
        userService.delete(userService.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserRecordDTO.UserView.UserPut.class)
                                             @JsonView(UserRecordDTO.UserView.UserPut.class) UserRecordDTO userRecordDTO){
        log.debug("PUT updateUser userRecordDTO received {}", userRecordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRecordDTO, userService.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                @RequestBody @Validated(UserRecordDTO.UserView.PasswordPut.class)
                                                @JsonView(UserRecordDTO.UserView.PasswordPut.class) UserRecordDTO userRecordDTO){

        log.debug("PUT updatePassword userId received {}", userId);
        log.debug("PUT updatePassword userRecordDTO received {}", userRecordDTO);

        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.get().getPassword().equals(userRecordDTO.oldPassword())){
            log.warn("Mismatched old password! userId {} ", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Passwords do not match");
        }
        userService.updatePassword(userRecordDTO, userModelOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserRecordDTO.UserView.ImagePut.class)
                                             @JsonView(UserRecordDTO.UserView.ImagePut.class) UserRecordDTO userRecordDTO){
        log.debug("PUT updateImage userRecordDTO received {}", userRecordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateImage(userRecordDTO, userService.findById(userId).get()));
    }
}
