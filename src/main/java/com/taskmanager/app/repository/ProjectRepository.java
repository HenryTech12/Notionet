package com.taskmanager.app.repository;

import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {

    Optional<ProjectModel> findByProjectName(String projectName);
    @Transactional
    @Modifying
    void deleteByProjectName(String projectName);
}
