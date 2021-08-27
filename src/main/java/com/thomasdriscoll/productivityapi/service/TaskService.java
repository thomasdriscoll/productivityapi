package com.thomasdriscoll.productivityapi.service;

import com.thomasdriscoll.productivityapi.lib.exceptions.DriscollException;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
import com.thomasdriscoll.productivityapi.lib.models.TaskRequest;
import com.thomasdriscoll.productivityapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){}

    public TaskDto createTask(String userId, TaskRequest newTaskRequest) throws DriscollException {
//        validateTask(newTaskRequest);
        return null;
    }

}
