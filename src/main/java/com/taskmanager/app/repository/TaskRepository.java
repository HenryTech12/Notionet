package com.taskmanager.app.repository;

import com.taskmanager.app.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskModel,Long> {

    Optional<List<TaskModel>> findByProjectName(String projectName);
}
