package com.taskmanager.app.service;

import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.mapper.ProjectMapper;
import com.taskmanager.app.mapper.TaskMapper;
import com.taskmanager.app.model.ProjectModel;
import com.taskmanager.app.model.TaskModel;
import com.taskmanager.app.notifications.NotificationResponse;
import com.taskmanager.app.repository.ProjectRepository;
import com.taskmanager.app.repository.TaskRepository;
import com.taskmanager.app.status.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MyProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MyTaskService myTaskService;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskRepository taskRepository;

    public void createProject(MyProject myProject) {
        myProject.setCreatedOn(new Date(System.currentTimeMillis()));
        ProjectModel projectModel =
                projectMapper.convertToModel(Objects.requireNonNull(myProject));
        initProjectStatus(projectModel);
        projectRepository.save(projectModel);
        log.info("project successfully created");
    }

    public MyProject getProject(Long id) {
        return projectMapper.convertToDTO(projectRepository
                .findById(id).orElse(new ProjectModel()));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
        log.info("project successfully removed from db.");
    }

    public void deleteProject(String productName) {
        projectRepository.deleteByProjectName(productName);
        log.info("project deleted successfully");
    }
    public List<MyProject> getAllProjects() {
        List<ProjectModel> projectModels =
                projectRepository.findAll();
        return projectModels.stream().
                map(data -> {
                    MyProject myProject =  projectMapper.convertToDTO(data);
                    if (!data.getTaskModelList().isEmpty()) {
                        myProject.setTask(taskMapper.convertToDTO(data.getTaskModelList().get(0)));
                    }
                    return myProject;
                }).toList();
    }

    public void updateProject(String projectName,MyProject data) {
        ProjectModel projectModel =
                projectRepository.findByProjectName(projectName).orElse(new ProjectModel());
        ProjectModel newProjectModel = projectMapper.convertToModel(data);
        newProjectModel.setId(projectModel.getId());
        projectRepository.save(newProjectModel);

        log.info("project details updated");
    }

    public void addTaskToProject(String projectName) {
        System.out.println("PROJECT NAME: "+projectName);
        ProjectModel projectModel =
                projectRepository.findByProjectName(projectName).orElse(new ProjectModel());
        List<TaskModel> taskModels = myTaskService.getTasks(projectName);
        if(!taskModels.isEmpty()) {
            projectModel.setTaskModelList(taskModels);
            projectRepository.save(projectModel);
            log.info("added task to project");
        }
    }

    public NotificationResponse updateTaskExpiration(String projectName) {
        NotificationResponse notificationResponse = new NotificationResponse();
        ProjectModel projectModel
                = projectRepository.findByProjectName(projectName).orElse(new ProjectModel());
        List<TaskModel> taskModels = projectModel.getTaskModelList();
        taskModels.stream()
                .map((data) -> {
                     LocalDate currentDate = LocalDate.now();
                     LocalDate taskExpectedDate = LocalDate.parse(data.getScheduledDate());

                     if(currentDate.equals(taskExpectedDate)) {
                         LocalTime currentTime = LocalTime.now();
                         LocalTime taskStartTime = LocalTime.parse(data.getStartTime());
                         LocalTime taskEndTime = LocalTime.parse(data.getEndTime());

                         if(currentTime.equals(taskStartTime) || currentTime.isAfter(taskStartTime)) {
                                notificationResponse.setMessage(
                                        "The Timer for Task : "+data.getTitle()+" has reached");

                         }
                         if(currentTime.isAfter(taskEndTime)) {
                                notificationResponse.setMessage(
                                        "The Task : "+data.getTitle()+ " has ended."
                                );
                                data.setStatus(TaskStatus.COMPLETED.name());
                                taskRepository.save(data); //updated status
                         }
                     }
                    return data;
                });
        return notificationResponse;
    }

    public void initProjectStatus(ProjectModel projectModel) {

        Date createdDate = projectModel.getCreatedOn();
        Date currentDate = new Date(System.currentTimeMillis());

        int result = currentDate.compareTo(createdDate);
        if(result == 0) {
            projectModel.setStatus("Recently");
        }
        else {
            if(result == 1) {
                projectModel.setStatus(result+" day ago");
            }
            else {
                projectModel.setStatus(result+" days ago");
            }
        }
    }
}
