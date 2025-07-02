package com.taskmanager.app.service;


import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.mapper.TaskMapper;
import com.taskmanager.app.model.ProjectModel;
import com.taskmanager.app.model.TaskModel;
import com.taskmanager.app.model.UserModel;
import com.taskmanager.app.repository.ProjectRepository;
import com.taskmanager.app.repository.TaskRepository;
import com.taskmanager.app.repository.UserRepository;
import com.taskmanager.app.status.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MyTaskService {


    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private UserRepository userRepository;
    public void createTask(MyTask myTask, String email) {
        if(!Objects.isNull(myTask)) {
            myTask.setStatus(TaskStatus.ONGOING.name());
            myTask.setCreatedWhen(LocalTime.now());
            myTask.setCreatedOn(LocalDate.now());

            ProjectModel projectModel = projectRepository.
                    findByProjectName(myTask.getProjectName())
                    .orElse(new ProjectModel());

            TaskModel taskModel = taskMapper.convertToModel(myTask);
            taskRepository.save(taskModel);

            UserModel userModel = userRepository.
                    findByEmail(email).orElse(new UserModel());

            List<TaskModel> tasks = projectModel.
                    getTasks();
            List<ProjectModel> projectModels = userModel.getProjects();

            if(!tasks.isEmpty()) {
                if(!taskExists(tasks,taskModel)) {
                    tasks.add(taskModel);
                    projectModel.setTasks(tasks);
                    projectRepository.save(projectModel);
                }
            }
            else {
                tasks = new ArrayList<>();
                tasks.add(taskModel);
                projectModel.setTasks(tasks);
                projectRepository.save(projectModel);
            }


            if(!projectModels.isEmpty()) {
                if(!projectExists(projectModels, projectModel)) {
                    projectModels.add(projectModel);
                    userModel.setProjects(projectModels);
                    userRepository.save(userModel);
                }
            }
            else {
                projectModels = new ArrayList<>();
                projectModels.add(projectModel);
                userModel.setProjects(projectModels);
                userRepository.save(userModel);
            }
         ;

            log.info("task saved to db.");
        }
    }

    public boolean projectExists(List<ProjectModel> projectModels, ProjectModel projectModel) {
        return projectModels.contains(projectModel);
    }

    public boolean taskExists(List<TaskModel> taskModels, TaskModel taskModel) {
        return taskModels.contains(taskModel);
    }

    public MyTask getTaskById(Long id) {
        return taskMapper.convertToDTO(
                taskRepository.findById(id).orElse(new TaskModel())
        );
    }

}
