package com.taskmanager.app.dto;

import com.taskmanager.app.model.TaskModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class MyProject {


    private Long id;
    private String projectName;
    private LocalDate createdOn;
    private String status;
    private byte[] imageBytes;
    private List<MyTask> tasks;

}
