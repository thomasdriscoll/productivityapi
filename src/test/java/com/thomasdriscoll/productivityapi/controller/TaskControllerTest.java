package com.thomasdriscoll.productivityapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasdriscoll.productivityapi.lib.models.TaskRequest;
import com.thomasdriscoll.productivityapi.lib.responses.DriscollResponse;
import com.thomasdriscoll.productivityapi.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    private String USER_ID = "userId";
    private String BAD_USER = "badUser";
    private TaskRequest TASK_REQUEST = TaskRequest.builder()
            .titleTask("Create template Go app")
            .descriptionTask("Want to create a Go app that I can reuse over and over to quickly get a project up and running")
            .priorityTask("high")
            .estimatedTimeTask(75)
            .typeTask("any")
            .statusType("backlog")
            .build();

    @Nested
    @DisplayName("Create Task tests")
    class CreateTaskDtoRequestTests {
        @Test
        public void givenTask_whenCreateTask_thenReturn201() throws Exception {
            String expected = new ObjectMapper().writeValueAsString(new DriscollResponse<>(HttpStatus.CREATED.value(), TASK_REQUEST));

            when(taskService.createTask(USER_ID, TASK_REQUEST)).thenReturn(null);
        }
    }
}