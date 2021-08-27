package com.thomasdriscoll.productivityapi.controller;

import com.thomasdriscoll.productivityapi.lib.exceptions.DriscollException;
import com.thomasdriscoll.productivityapi.lib.models.Task;
import com.thomasdriscoll.productivityapi.lib.models.TaskRequest;
import com.thomasdriscoll.productivityapi.lib.responses.DriscollResponse;
import com.thomasdriscoll.productivityapi.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/tasks")
public class TaskController {
    TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<DriscollResponse<Task>> createTask(
            @PathVariable String userId,
            @RequestBody TaskRequest newTaskRequest) throws DriscollException {
        return ResponseEntity.status(HttpStatus.CREATED).body(new DriscollResponse<Task>(HttpStatus.CREATED.value(), taskService.createTask(userId, newTaskRequest)));
    }
}
