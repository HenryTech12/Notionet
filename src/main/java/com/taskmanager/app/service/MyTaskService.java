package com.taskmanager.app.service;

import com.taskmanager.app.dto.MyTask;
import com.taskmanager.app.mapper.TaskMapper;
import com.taskmanager.app.model.TaskModel;
import com.taskmanager.app.repository.TaskRepository;
import com.taskmanager.app.status.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MyTaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskRepository taskRepository;

    public void createTask(MyTask myTask) {
        myTask.setCreatedOn(LocalDate.now());
        myTask.setCreatedWhen(LocalTime.now());
        myTask.setStatus(TaskStatus.ONGOING.name()); // initially

        taskRepository.save(taskMapper.convertToModel(myTask));
        log.info("task added to db.");
    }

    public List<MyTask> getTasks() {
        return
                taskRepository.findAll()
                        .stream()
                        .map((taskMapper::convertToDTO))
                        .toList();
    }

    public MyTask getTask(Long id) {
        return taskMapper.convertToDTO(taskRepository.
                findById(id).orElse(new TaskModel()));
    }

    public List<TaskModel> getTasks(String projectName) {
        return taskRepository.findByProjectName(projectName).
                orElse(new ArrayList<>());
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
        log.info("'task removed from db.");
    }

    public List<MyTask> convertListOfModelsToDTOs(List<TaskModel> taskModelList) {
        if(!taskModelList.isEmpty())
            return taskModelList.stream()
                .map(taskMapper::convertToDTO)
                .toList();
        return new ArrayList<>();
    }

    public void updateTask(Long id, MyTask myTask) {
        TaskModel taskModel = taskRepository.findById(id).orElse(new TaskModel());
        TaskModel newTaskModel = taskMapper.convertToModel(myTask);
        newTaskModel.setId(taskModel.getId());
        taskRepository.save(newTaskModel);

        log.info("updated task details");
    }
}
