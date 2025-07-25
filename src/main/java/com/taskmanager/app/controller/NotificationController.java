package com.taskmanager.app.controller;

import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.dto.UserData;
import com.taskmanager.app.notifications.NotificationResponse;
import com.taskmanager.app.service.MyProjectService;
import com.taskmanager.app.service.MyTaskService;
import com.taskmanager.app.service.UserService;
import com.taskmanager.app.status.TaskStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class NotificationController {

    @Autowired
    private MyProjectService myProjectService;
    @Autowired
    private MyTaskService myTaskService;
    @Autowired
    private UserService userService;

    @MessageMapping("/update")
    @SendTo("/topics/uproject")
    public NotificationResponse notifyProject(@RequestBody MyProject myProject, OAuth2AuthenticationToken token) {
        OAuth2User oAuth2User = token.getPrincipal();
        UserData userData = userService.findByEmail(oAuth2User.getAttribute("email"));
        myProjectService.checkForStatus(myProject.getProjectName(), userData);
        return myProjectService.checkForStatus(myProject.getProjectName(), userData);
    }

    @MessageMapping("/getStatus")
    @SendTo("/topics/len")
    public MyResponse getStatus(@RequestBody MyProject myProject) {
        System.out.println(myProject.getProjectName());
        List<MyTask> otList = myTaskService.getTaskViaProjectNameAndStatus(myProject.getProjectName(), TaskStatus.ONGOING.name());
        List<MyTask> ctList = myTaskService.getTaskViaProjectNameAndStatus(myProject.getProjectName(), TaskStatus.COMPLETED.name());
         MyResponse myResponse = new MyResponse();
         myResponse.setCT(ctList.size());
         myResponse.setOT(otList.size());
         return  myResponse;
    }

    @Data
    public static class MyResponse {
        private int oT;
        private int cT;
    }
}
