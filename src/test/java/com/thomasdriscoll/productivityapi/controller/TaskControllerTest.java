package com.thomasdriscoll.productivityapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasdriscoll.productivityapi.lib.enums.PriorityTask;
import com.thomasdriscoll.productivityapi.lib.enums.StatusType;
import com.thomasdriscoll.productivityapi.lib.enums.TypeTask;
import com.thomasdriscoll.productivityapi.lib.exceptions.DriscollException;
import com.thomasdriscoll.productivityapi.lib.exceptions.TaskExceptions;
import com.thomasdriscoll.productivityapi.lib.exceptions.UserExceptions;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
import com.thomasdriscoll.productivityapi.lib.models.TaskRequest;
import com.thomasdriscoll.productivityapi.lib.responses.DriscollResponse;
import com.thomasdriscoll.productivityapi.service.TaskService;
import com.thomasdriscoll.productivityapi.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final String USER_ID = "userId";
    private final String BAD_USER = "badUser";
    private final String TITLE_TASK = "Create template Go app";
    private final String DESCRIPTION_TASK = "Want to create a Go app that I can reuse over and over to quickly get a project up and running";
    private final String PRIORITY_TASK = PriorityTask.HIGH.getPriority();
    private final Integer ESTIMATED_TIME_TASK = 75;
    private final String TASK_TYPE = TypeTask.INTELLECTUAL.getType();
    private final String STATUS_TYPE = StatusType.BACKLOG.getStatus();

    private final TaskRequest TASK_REQUEST = TaskRequest.builder()
            .titleTask(TITLE_TASK)
            .descriptionTask(DESCRIPTION_TASK)
            .priorityTask(PRIORITY_TASK)
            .estimatedTimeTask(ESTIMATED_TIME_TASK)
            .typeTask(TASK_TYPE)
            .statusType(STATUS_TYPE)
            .build();

    private final TaskRequest TASK_REQUEST_INVALID_PRIORITY = TaskRequest.builder()
            .titleTask(TITLE_TASK)
            .descriptionTask(DESCRIPTION_TASK)
            .priorityTask("junk")
            .estimatedTimeTask(ESTIMATED_TIME_TASK)
            .typeTask(TASK_TYPE)
            .statusType(STATUS_TYPE)
            .build();

    private final TaskRequest TASK_REQUEST_INVALID_TYPE = TaskRequest.builder()
            .titleTask(TITLE_TASK)
            .descriptionTask(DESCRIPTION_TASK)
            .priorityTask(PRIORITY_TASK)
            .estimatedTimeTask(ESTIMATED_TIME_TASK)
            .typeTask("junk")
            .statusType(STATUS_TYPE)
            .build();

    private final TaskRequest TASK_REQUEST_INVALID_STATUS = TaskRequest.builder()
            .titleTask(TITLE_TASK)
            .descriptionTask(DESCRIPTION_TASK)
            .priorityTask(PRIORITY_TASK)
            .estimatedTimeTask(ESTIMATED_TIME_TASK)
            .typeTask(TASK_TYPE)
            .statusType("junk")
            .build();

    private final TaskDto TASK_DTO = TaskDto.builder()
            .userId(USER_ID)
            .titleTask(TITLE_TASK)
            .descriptionTask(DESCRIPTION_TASK)
            .priorityTask(PriorityTask.HIGH)
            .estimatedTimeTask(ESTIMATED_TIME_TASK)
            .typeTask(TypeTask.INTELLECTUAL)
            .statusType(StatusType.BACKLOG)
            .build();

    @Nested
    @DisplayName("Create Task tests")
    class CreateTaskDtoRequestTests {
        ObjectMapper mapper = new ObjectMapper();


        CreateTaskDtoRequestTests() throws JsonProcessingException {
        }

        @Test
        public void givenTask_whenCreateTask_thenReturn201() throws Exception {
            String request = mapper.writeValueAsString(TASK_REQUEST);
            String expected = mapper.writeValueAsString(new DriscollResponse<>(HttpStatus.CREATED.value(), TASK_DTO));

            when(taskService.createTask(USER_ID, TASK_REQUEST)).thenReturn(TASK_DTO);

            MvcResult result = mockMvc.perform(post(String.format("/users/%s/tasks", USER_ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isCreated())
                    .andReturn();
            String actual = result.getResponse().getContentAsString();
            assertEquals(expected, actual);
        }

        @Test
        public void givenTask_whenInvalidUser_return404() throws Exception {
            String request = mapper.writeValueAsString(TASK_REQUEST);
            DriscollException exception = new DriscollException(UserExceptions.USER_NOT_FOUND.getStatus(), UserExceptions.USER_NOT_FOUND.getMessage());
            String expected = mapper.writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            doThrow(exception).when(userService).validateUser(BAD_USER);

            //Do test
            MvcResult result = mockMvc.perform(post(String.format("/users/%s/tasks", BAD_USER))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isNotFound())
                    .andReturn();

            //Assert if test worked
            assertEquals(expected, result.getResponse().getContentAsString());
        }

        @Test
        public void givenTask_whenInvalidPriority_return400() throws Exception {
            String request = mapper.writeValueAsString(TASK_REQUEST_INVALID_PRIORITY);
            DriscollException exception = new DriscollException(TaskExceptions.INVALID_TASK_PRIORITY.getStatus(), TaskExceptions.INVALID_TASK_PRIORITY.getMessage());
            String expected = mapper.writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            when(taskService.createTask(USER_ID, TASK_REQUEST_INVALID_PRIORITY)).thenThrow(exception);

            MvcResult result = mockMvc.perform(post(String.format("/users/%s/tasks", USER_ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(expected, result.getResponse().getContentAsString());
        }

        @Test
        public void givenTask_whenInvalidType_return400() throws Exception {
            String request = mapper.writeValueAsString(TASK_REQUEST_INVALID_TYPE);
            DriscollException exception = new DriscollException(TaskExceptions.INVALID_TASK_TYPE.getStatus(), TaskExceptions.INVALID_TASK_TYPE.getMessage());
            String expected = mapper.writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            when(taskService.createTask(USER_ID, TASK_REQUEST_INVALID_TYPE)).thenThrow(exception);

            MvcResult result = mockMvc.perform(post(String.format("/users/%s/tasks", USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andReturn();

            assertEquals(expected, result.getResponse().getContentAsString());
        }

        @Test
        public void givenTask_whenInvalidStatus_return400() throws Exception {
            String request = mapper.writeValueAsString(TASK_REQUEST_INVALID_STATUS);
            DriscollException exception = new DriscollException(TaskExceptions.INVALID_STATUS.getStatus(), TaskExceptions.INVALID_STATUS.getMessage());
            String expected = mapper.writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            when(taskService.createTask(USER_ID, TASK_REQUEST_INVALID_STATUS)).thenThrow(exception);

            MvcResult result = mockMvc.perform(post(String.format("/users/%s/tasks", USER_ID))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(expected, result.getResponse().getContentAsString());
        }
    }
}