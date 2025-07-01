package com.taskmanager.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {

    private String email;
    private String name;
    private String picture;
    private String joined;
    private List<MyProject> projects;
}
