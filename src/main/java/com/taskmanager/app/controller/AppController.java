package com.taskmanager.app.controller;

import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.model.TaskModel;
import com.taskmanager.app.service.MyProjectService;
import com.taskmanager.app.service.MyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired
    private MyProjectService myProjectService;
    @Autowired
    private MyTaskService myTaskService;

    //dashboard page
    @GetMapping("/dashboard")
    public String getDashboardPage(Model model) {
        List<MyProject> projectList = myProjectService.getAllProjects();
        projectList.forEach(System.out::println);
        model.addAttribute("projects", projectList);
        if (!projectList.isEmpty())
            model.addAttribute("projectName",projectList.get(0).getProjectName());
        model.addAttribute("project", new MyProject());
        return "main";
    }

    //create Task page
    @GetMapping("/task")
    public String getCreateTaskPage(Model model) {
        model.addAttribute("task", new MyTask());
        model.addAttribute("projects", myProjectService.getAllProjects());
        model.addAttribute("project", new MyProject());
        return "addTask";
    }

    @PostMapping("/project/delete")
    public RedirectView deleteProject(@RequestParam String name) {
        System.out.println(name);
        myProjectService.deleteProject(name);
        return new RedirectView("/app/dashboard");
    }

    @GetMapping("/task/view/{projectId}")
    public String  getTaskWithID(@PathVariable Long projectId, Model model) {
        MyProject myProject = myProjectService.getProject(projectId);
        List<TaskModel> tasks = myTaskService.getTasks(myProject.getProjectName());
        if (!tasks.isEmpty()) {
            model.addAttribute("tasks",myTaskService.convertListOfModelsToDTOs(tasks));
            model.addAttribute("projectName",
                    myTaskService.convertListOfModelsToDTOs(tasks).get(0).getProjectName());
        }
        model.addAttribute("task", new MyTask());
        return "task";
    }

    @GetMapping("/tasks")
    public String getTasksPage(Model model) {
        List<MyTask> tasks = myTaskService.getTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectName",tasks.get(0).getProjectName());
        model.addAttribute("task", new MyTask());
        return "task";
    }


    @PostMapping("/task/create")
    public RedirectView addTask(Model model, @ModelAttribute MyTask task) {
        myTaskService.createTask(task);
        return new RedirectView("/app/tasks");
    }

    @GetMapping("/project")
    public String getCreateProjectPage(Model model) {
            model.addAttribute("project", new MyProject());
            return "createProject";
    }


    @GetMapping("/task/viewEdit/{id}")
    public String editTask(@PathVariable Long id,Model model) {
        model.addAttribute("task", myTaskService.getTask(id));
        model.addAttribute("projects", myProjectService.getAllProjects());
        model.addAttribute("project", new MyProject());
        model.addAttribute("taskID",id);
        return "addTask";
    }

    @PostMapping("/task/update/{id}")
    public RedirectView updateTask(@PathVariable Long id, @ModelAttribute MyTask task) {
        myTaskService.updateTask(id,task);
        return new RedirectView("/app/tasks");
    }

    @PostMapping("/project/create")
    public RedirectView createProject(@ModelAttribute MyProject project) {
        myProjectService.createProject(project);
        return new RedirectView("/app/dashboard");
    }

    @GetMapping("/task/show")
    public String getShowTaskPage() {
        return "task";
    }
}
