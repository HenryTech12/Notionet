package com.taskmanager.app.dto;

import com.taskmanager.app.model.TaskModel;
import lombok.Data;

import java.util.Date;

@Data
public class MyProject {


    private String projectName;
    private Date createdOn;
    private String status;
    private MyTask task;
}
