package com.taskmanager.app.repository;

import com.taskmanager.app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserModel,Long> {

    Optional<UserModel> findByEmail(String email);
}
