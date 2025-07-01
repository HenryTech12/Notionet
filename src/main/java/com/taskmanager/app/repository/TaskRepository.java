package com.taskmanager.app.repository;

import com.taskmanager.app.model.TaskModel;
import com.taskmanager.app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskModel,Long> {


}