package com.taskmanager.app.controller;


import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.service.MyProjectService;
import com.taskmanager.app.service.MyTaskService;
import com.taskmanager.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired
    private MyProjectService myProjectService;
    @Autowired
    private MyTaskService myTaskService;
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model, OAuth2AuthenticationToken token) {
        OAuth2User auth2User = Objects
                .requireNonNull(token).getPrincipal();
        userService.addUser(userService.extractUserDetails(auth2User));
        List<MyProject> projects = myProjectService.getProjects(auth2User.getAttribute("email"));
        model.addAttribute("projects",projects);
        model.addAttribute("data", new MyProject());
        model.addAttribute("name",auth2User.getAttribute("name"));
        return "main";
    }

    @GetMapping("/task")
    public String createTaskPage(Model model, OAuth2AuthenticationToken token) {
        List<MyProject> myProjects = myProjectService.getProjects(
                Objects.requireNonNull(token)
                        .getPrincipal().getAttribute("email")
        );
        model.addAttribute("projects",myProjects);
        model.addAttribute("task", new MyTask());
        return "addTask";
    }

    @GetMapping("/project")
    public String createProjectPage(Model model) {
        model.addAttribute("project", new MyProject());
        return "createProject";
    }

    @PostMapping("/project/create")
    public RedirectView addProject(@ModelAttribute MyProject myProject, OAuth2AuthenticationToken token) {
        myProjectService.createProject(myProject,
                Objects.requireNonNull(token).getPrincipal()
                        .getAttribute("email"));
        return new RedirectView("/app/dashboard");
    }

    @PostMapping("/task/create")
    public RedirectView addTask(@ModelAttribute MyTask task, OAuth2AuthenticationToken token) {
        myTaskService.createTask(task,
                Objects.requireNonNull(token)
                        .getPrincipal().getAttribute("email"));
        return new RedirectView("/app/dashboard");
    }

    @GetMapping("/task/view/{projectId}")
    public String getProject(@PathVariable Long projectId, Model model) {
        MyProject myProject = myProjectService.getProject(projectId);
        model.addAttribute("projectId",myProject.getId());
        model.addAttribute("projectName",myProject.getProjectName());
        model.addAttribute("tasks",myProject.getTasks());
        model.addAttribute("task", new MyTask());
        return "task";
    }

    @GetMapping("/task/viewEdit/{taskID}")
    public String viewTask(@PathVariable Long taskID, OAuth2AuthenticationToken token,Model model) {
        List<MyProject> projects = myProjectService.
                getProjects(Objects.requireNonNull(token)
                        .getPrincipal().getAttribute("email"));
        model.addAttribute("task",myTaskService.getTaskById(taskID));
        model.addAttribute("projects", projects);
        return "addTask";
    }

    @GetMapping("/project/delete/{projectId}")
    public RedirectView removeProjectByID(@PathVariable Long projectId) {
        myProjectService.deleteByProject(projectId);
        return new RedirectView("/app/dashboard");
    }

    @GetMapping("/project/delete/{name}")
    public RedirectView removeProjectByName(@PathVariable String name) {
        myProjectService.deleteProjectByName(name);
        return new RedirectView("/app/dashboard");
    }
}
