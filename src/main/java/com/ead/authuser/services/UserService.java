package com.ead.authuser.services;

import com.ead.authuser.dtos.UserRecordDTO;
import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserModel> findAll();

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userModel);

    UserModel registerUser(UserRecordDTO userRecordDTO);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    UserModel updateUser(UserRecordDTO userRecordDTO, UserModel userModel);

    UserModel updatePassword(UserRecordDTO userRecordDTO, UserModel userModel);

    UserModel updateImage(UserRecordDTO userRecordDTO, UserModel userModel);

    Page<UserModel> findAll(Pageable pageable);
}
