package com.taskmanager.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class ProjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String projectName;
    private Date createdOn;
    private String status; // days project existed
    @OneToMany(fetch = FetchType.EAGER)
    private List<TaskModel> taskModelList;
}
