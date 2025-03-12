package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseRecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserCourseController {

    final CourseClient courseClient;

    @GetMapping("/users/{userId}/courses")
     public ResponseEntity<Page<CourseRecordDTO>> getAllCoursesByUser(@PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
                                                     @PathVariable(value = "userId") UUID userId){
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(pageable, userId));
    }
}
