package com.taskmanager.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String endTime;
    private String startTime;
    private String scheduledDate;
    private String status;
    private LocalDate createdOn;
    private String category;
    private LocalTime createdWhen;
    private String projectName;
    private String description;
}
