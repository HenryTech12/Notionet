package com.taskmanager.app.events;

import com.taskmanager.app.model.ProjectModel;
import com.taskmanager.app.repository.ProjectRepository;
import com.taskmanager.app.service.MyProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ProjectEvent {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MyProjectService myProjectService;
    private List<ProjectModel> projectModels = null;

    //@Scheduled(fixedDelay = 86400)
    public void updateStatus() {
        if(projectModels == null) {
            projectModels
                    = projectRepository.findAll();
            System.out.println("fetching...");
        } else {
            projectModels.forEach((data) -> {
                //myProjectService.initProjectStatus(data);
                projectRepository.save(data);
                log.info("updated project status");
            });
        }
    }

}
