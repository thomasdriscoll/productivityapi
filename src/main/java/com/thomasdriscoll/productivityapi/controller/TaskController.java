package com.thomasdriscoll.productivityapi.controller;

import com.thomasdriscoll.productivityapi.lib.exceptions.DriscollException;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
import com.thomasdriscoll.productivityapi.lib.models.TaskRequest;
import com.thomasdriscoll.productivityapi.lib.responses.DriscollResponse;
import com.thomasdriscoll.productivityapi.service.TaskService;
import com.thomasdriscoll.productivityapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/tasks")
public class TaskController {
    TaskService taskService;
    UserService userService;

    public TaskController(
            TaskService taskService,
            UserService userService
    ){
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<DriscollResponse<TaskDto>> createTask (
            @PathVariable String userId,
            @RequestBody TaskRequest newTaskRequest) throws DriscollException {
        userService.validateUser(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DriscollResponse<>(HttpStatus.CREATED.value(), taskService.createTask(userId, newTaskRequest)));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<DriscollResponse<TaskDto>> getTaskById (
            @PathVariable String userId,
            @PathVariable Long taskId
    ) throws DriscollException {
        userService.validateUser(userId); // 404 if no such user
        return ResponseEntity.ok(new DriscollResponse<>(HttpStatus.OK.value(), taskService.getTaskById(userId, taskId)));
    }
}
