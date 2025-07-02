package com.taskmanager.app.service;


import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.dto.UserData;
import com.taskmanager.app.mapper.ProjectMapper;
import com.taskmanager.app.model.ProjectModel;
import com.taskmanager.app.model.TaskModel;
import com.taskmanager.app.model.UserModel;
import com.taskmanager.app.notifications.NotificationResponse;
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

@Service
@Slf4j
public class MyProjectService {

    NotificationResponse notificationResponse = new NotificationResponse();
    List<String> messages = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private TaskRepository taskRepository;

    public List<MyProject> getProjects(String email) {
        UserModel userModel = userRepository.findByEmail(email).orElse(new UserModel());
        if(!Objects.isNull(userModel.getProjects()))
            return userModel.getProjects()
                .stream().map(projectMapper::convertToDTO).toList();
        else
            return null;
    }

    public void createProject(MyProject myProject, String email) {
        if(!Objects.isNull(myProject)) {
            myProject.setCreatedOn(LocalDate.now());
            myProject.setStatus("Just Now...");
            ProjectModel projectModel = projectMapper.convertToModel(myProject);
            UserModel userModel = userRepository.findByEmail(email)
                            .orElse(new UserModel());
            List<ProjectModel> projectModels = userModel.getProjects();
            if(!projectModels.isEmpty()) {
                projectModels.add(projectModel);
            }
            else {
                projectModels = new ArrayList<>();
                projectModels.add(projectModel);
            }
            projectRepository.save(projectModel);
            userModel.setProjects(projectModels);
            userRepository.save(userModel); // updates user data
            log.info("project created....");
        }
    }



    public MyProject getProject(Long projectId) {
        ProjectModel projectModel =
                projectRepository.findById(projectId)
                        .orElse(new ProjectModel());
        return projectMapper.convertToDTO(projectModel);
    }

    public void deleteByProject(Long projectId) {
        projectRepository.deleteById(projectId);
        log.info("project removed from db.");
    }

    public NotificationResponse checkForStatus(String projectName, UserData userData) {
        MailService.MailInfo mailInfo = new MailService.MailInfo();
        mailInfo.setTo(userData.getEmail());
        mailInfo.setSubject("Notionet - Task Update (Notification)");
        ProjectModel projectModel = projectRepository.findByProjectName(projectName)
                .orElse(new ProjectModel());
        List<TaskModel> taskModels =  projectModel.getTasks();
        for(TaskModel taskModel : taskModels) {
           LocalDate currentDate = LocalDate.now();
           LocalDate scheduledDate = LocalDate.parse(taskModel.getScheduledDate());
            System.out.println("SCHEDULED DATE: "+scheduledDate);

           if(currentDate.isEqual(scheduledDate)) {

               LocalTime currentTime = LocalTime.now();
               LocalTime startTime = LocalTime.parse(taskModel.getStartTime());
               LocalTime endTime = LocalTime.parse(taskModel.getEndTime());
                if(taskModel.getStatus().equals(TaskStatus.ONGOING.name())) {
                    if(currentTime.isAfter(startTime) || currentTime.equals(startTime)) {
                        mailInfo.setMessage(String.format("""
                           Hey "%s",
                           
                           Just a quick update — your task "%s" has officially started!
                           
                           You can keep an eye on its progress in your project space.
                           
                           Cheers, \s
                           The Notionet Team
                           
                           """,userData.getName(),taskModel.getTitle()));
                        mailService.sendMessage(mailInfo);
                        messages.add("The Task: "+taskModel.getTitle()+" has started");
                    }
                    if(currentTime.isAfter(endTime) || currentTime.equals(endTime)) {
                        mailInfo.setMessage(String.format(
                                "Hi %s,\n\nYour task \"%s\" has ended successfully.\n\nYou can review the results or mark it as complete in your dashboard.\n\nBest regards,\nThe Notionet Team",
                                userData.getName(), taskModel.getTitle()
                        ));
                        mailService.sendMessage(mailInfo);
                        messages.add("The Task: "+taskModel.getTitle()+" has ended.");
                        taskModel.setStatus(TaskStatus.COMPLETED.name());
                        taskRepository.save(taskModel);
                    }
                }
           }
        }
        notificationResponse.setMessage(messages);
        return notificationResponse;
    }

    public void deleteProjectByName(String projectName) {
        projectRepository.deleteByProjectName(projectName);
    }
}
