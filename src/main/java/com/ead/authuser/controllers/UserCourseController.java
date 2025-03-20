package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseRecordDTO;
import com.ead.authuser.dtos.UserCourseRecordDTO;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserCourseController {

    final CourseClient courseClient;
    final UserService userService;
    final UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
     public ResponseEntity<Page<CourseRecordDTO>> getAllCoursesByUser(@PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
                                                     @PathVariable(value = "userId") UUID userId){
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(pageable, userId));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "userId") UUID userId,
                                                               @RequestBody @Valid UserCourseRecordDTO userCourseRecordDTO){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseRecordDTO.courseId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists");
        }

        UserCourseModel userCourseModel = userCourseService.saveSubscription(
                userModelOptional.get().convertToUserCourseModel(userCourseRecordDTO.courseId())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }



}
