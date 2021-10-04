package com.thomasdriscoll.productivityapi.controller;

import com.thomasdriscoll.productivityapi.lib.exceptions.DriscollException;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
import com.thomasdriscoll.productivityapi.lib.responses.DriscollResponse;
import com.thomasdriscoll.productivityapi.service.TaskService;
import com.thomasdriscoll.productivityapi.service.UserService;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/board")
public class BoardController {
    TaskService taskService;
    UserService userService;

    public BoardController (
            TaskService taskService,
            UserService userService
    ) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<DriscollResponse<List<List<TaskDto>>>> getTasksOnBoard(@PathVariable String userId) throws DriscollException {
        userService.validateUser(userId);
        return ResponseEntity.ok(new DriscollResponse<>(HttpStatus.OK.value(), taskService.getTasksOnBoard(userId)));
    }
}
