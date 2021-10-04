package com.thomasdriscoll.productivityapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasdriscoll.productivityapi.lib.enums.PriorityTask;
import com.thomasdriscoll.productivityapi.lib.enums.StatusType;
import com.thomasdriscoll.productivityapi.lib.enums.TypeTask;
import com.thomasdriscoll.productivityapi.lib.exceptions.DriscollException;
import com.thomasdriscoll.productivityapi.lib.exceptions.UserExceptions;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BoardController.class)
class BoardControllerTest {
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
    private final Integer ESTIMATED_TIME_TASK = 75;

    private final TaskDto TASK_DTO = TaskDto.builder()
            .userId(USER_ID)
            .titleTask(TITLE_TASK)
            .descriptionTask(DESCRIPTION_TASK)
            .priorityTask(PriorityTask.HIGH)
            .estimatedTimeTask(ESTIMATED_TIME_TASK)
            .typeTask(TypeTask.INTELLECTUAL)
            .statusType(StatusType.BACKLOG)
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    @Nested
    @DisplayName("Get Tasks on Board tests")
    class GetTasksOnBoard {
        @Test
        public void givenUserId_whenGetTasksOnBoard_thenReturn200() throws Exception {
            String expected = mapper.writeValueAsString(new DriscollResponse<>(HttpStatus.OK.value(), List.of(List.of(TASK_DTO))));

            when(taskService.getTasksOnBoard(USER_ID)).thenReturn(List.of(List.of(TASK_DTO)));

            MvcResult result = mockMvc.perform(get(String.format("/users/%s/board/tasks", USER_ID))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

            String actual = result.getResponse().getContentAsString();
            assertEquals(expected, actual);
        }

        @Test
        public void givenUserId_whenInvalidUser_thenReturn404() throws Exception {
            DriscollException exception = new DriscollException(UserExceptions.USER_NOT_FOUND.getStatus(), UserExceptions.USER_NOT_FOUND.getMessage());
            String expected = mapper.writeValueAsString(new DriscollResponse<>(exception.getStatus().value(), exception.getMessage()));

            //Mock what needs to be mocked
            doThrow(exception).when(userService).validateUser(BAD_USER);

            //Do test
            MvcResult result = mockMvc.perform(get(String.format("/users/%s/board/tasks", BAD_USER))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andReturn();

            //Assert if test worked
            assertEquals(expected, result.getResponse().getContentAsString());
        }

        @Test
        public void givenUserId_whenNoTasks_returnEmptyList() throws Exception {
            String expected = mapper.writeValueAsString(new DriscollResponse<>(HttpStatus.OK.value(), Collections.emptyList()));

            when(taskService.getTasksOnBoard(USER_ID)).thenReturn(Collections.emptyList());

            MvcResult result = mockMvc.perform(get(String.format("/users/%s/board/tasks", USER_ID))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = result.getResponse().getContentAsString();
            assertEquals(expected, actual);
        }
    }
}