package com.taskmanager.app.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
public class MyTask {

    private String title;
    private String startTime;
    private String endTime;
    private String status;
    private LocalDate createdOn;
    private String category;
    private String scheduledDate;
    private LocalTime createdWhen;
    private String projectName;
    private String description;
}
