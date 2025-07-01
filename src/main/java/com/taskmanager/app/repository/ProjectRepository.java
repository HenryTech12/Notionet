package com.taskmanager.app.repository;

import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.model.ProjectModel;
import com.taskmanager.app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {

    Optional<ProjectModel> findByProjectName(String projectName);

    @Modifying
    @Transactional
    void deleteByProjectName(String projectName);
}
