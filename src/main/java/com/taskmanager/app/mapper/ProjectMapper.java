package com.taskmanager.app.mapper;

import com.taskmanager.app.dto.MyProject;
import com.taskmanager.app.model.ProjectModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProjectMapper {

    @Autowired
    private ModelMapper modelMapper;

    public MyProject convertToDTO(ProjectModel projectModel) {
        if(!Objects.isNull(projectModel))
            return modelMapper.map(projectModel, MyProject.class);
        return null;
    }

    public ProjectModel convertToModel(MyProject myProject) {
        if(!Objects.isNull(myProject))
            return modelMapper.map(myProject, ProjectModel.class);
        return null;
    }
}
