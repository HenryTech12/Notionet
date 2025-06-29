package com.taskmanager.app.controller;

import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.notifications.NotificationResponse;
import com.taskmanager.app.service.MyProjectService;
import com.taskmanager.app.service.MyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private MyProjectService myProjectService;
    @Autowired
    private MyTaskService myTaskService;

    @MessageMapping("/update")
    @SendTo("/topics/uproject")
    public String notifyProject(@RequestBody MyProject myProject) {
        myProjectService.addTaskToProject(myProject.getProjectName());
        return "Updated";
    }

    @MessageMapping("/getLimit")
    @SendTo("/topics/finished")
    public NotificationResponse checkExpiration(@RequestBody MyProject myProject) {
        return myProjectService.updateTaskExpiration(myProject.getProjectName());
    }
}
