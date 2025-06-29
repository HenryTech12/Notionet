package com.taskmanager.app.mapper;

import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.model.TaskModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TaskMapper {

    @Autowired
    private ModelMapper mapper;

    public TaskModel convertToModel(MyTask myTask) {
        if(!Objects.isNull(myTask))
            return mapper.map(myTask,TaskModel.class);
        return null;
    }

    public MyTask convertToDTO(TaskModel taskModel) {
        if(!Objects.isNull(taskModel))
            return mapper.map(taskModel, MyTask.class);
        return null;
    }
}
