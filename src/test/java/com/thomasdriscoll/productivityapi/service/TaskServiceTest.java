package com.thomasdriscoll.productivityapi.service;

import com.thomasdriscoll.productivityapi.lib.enums.PriorityTask;
import com.thomasdriscoll.productivityapi.lib.enums.StatusType;
import com.thomasdriscoll.productivityapi.lib.enums.TypeTask;
import com.thomasdriscoll.productivityapi.lib.exceptions.DriscollException;
import com.thomasdriscoll.productivityapi.lib.exceptions.TaskExceptions;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
import com.thomasdriscoll.productivityapi.lib.models.TaskRequest;
import com.thomasdriscoll.productivityapi.repository.TaskDao;
import com.thomasdriscoll.productivityapi.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TaskServiceTest {
    @MockBean
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    private final String USER_ID = "userId";
    private final String BAD_USER = "badUser";
    private final String TITLE_TASK = "Create template Go app";
    private final String DESCRIPTION_TASK = "Want to create a Go app that I can reuse over and over to quickly get a project up and running";
    private final String PRIORITY_TASK = PriorityTask.HIGH.getPriority();
    private final Integer ESTIMATED_TIME_TASK = 75;
    private final String TASK_TYPE = TypeTask.INTELLECTUAL.getType();
    private final String STATUS_TYPE = StatusType.BACKLOG.getStatus();
    private final Long TASK_ID = 1234567890L;
    private final Long BAD_TASK_ID = 9876543210L;

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

    private final TaskDao TASK_DAO = new TaskDao(TASK_DTO);

    @Nested
    @DisplayName("Create Task service tests")
    class CreateTaskTests {

        @Test
        public void validateTaskRequest_goldenPath() throws Exception {
            when(taskRepository.save(TASK_DAO)).thenReturn(TASK_DAO);
            TaskDto actual = taskService.createTask(USER_ID, TASK_REQUEST);
            assertEquals(TASK_DTO, actual);
        }

        @Test
        public void validateTaskRequest_throwsOnPriority() throws Exception {
            DriscollException excepted = new DriscollException(TaskExceptions.INVALID_TASK_PRIORITY.getStatus(), TaskExceptions.INVALID_TASK_PRIORITY.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.createTask(USER_ID, TASK_REQUEST_INVALID_PRIORITY));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void validateTaskRequest_throwsOnTaskType() throws Exception {
            DriscollException excepted = new DriscollException(TaskExceptions.INVALID_TASK_TYPE.getStatus(), TaskExceptions.INVALID_TASK_TYPE.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.createTask(USER_ID, TASK_REQUEST_INVALID_TYPE));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void validateTaskRequest_throwsOnStatusType() throws Exception {
            DriscollException excepted = new DriscollException(TaskExceptions.INVALID_STATUS.getStatus(), TaskExceptions.INVALID_STATUS.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.createTask(USER_ID, TASK_REQUEST_INVALID_STATUS));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void validTask_saveToRepository_goldenPath() throws Exception {
            TaskDto actual = taskService.createTask(USER_ID, TASK_REQUEST);
            verify(taskRepository).save(any(TaskDao.class));
        }
    }

    @Nested
    @DisplayName("Get Task By Id Service tests")
    class GetTaskByIdTests {
        @Test
        public void validTaskId_thenReturnTaskDto() throws Exception {
            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.of(TASK_DAO));
            TaskDto actual = taskService.getTaskById(USER_ID, TASK_ID);
            assertEquals(TASK_DTO, actual);
        }

        @Test
        public void invalidTaskId_throw404Error() throws Exception {
            DriscollException excepted = new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());

            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());

            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.getTaskById(USER_ID, BAD_TASK_ID));

            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }
    }

    @Nested
    @DisplayName("Update Tasks Tests")
    class UpdateTasksTests {
        @Test
        public void validateTaskRequest_goldenPath() throws Exception {
            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.of(TASK_DAO));
            when(taskRepository.save(TASK_DAO)).thenReturn(TASK_DAO);
            TaskDto actual = taskService.updateTask(USER_ID, TASK_ID, TASK_REQUEST);
            assertEquals(TASK_DTO, actual);
        }

        @Test
        public void validateTaskRequest_throwsOnPriority() throws Exception {
            DriscollException excepted = new DriscollException(TaskExceptions.INVALID_TASK_PRIORITY.getStatus(), TaskExceptions.INVALID_TASK_PRIORITY.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTask(USER_ID, TASK_ID, TASK_REQUEST_INVALID_PRIORITY));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void validateTaskRequest_throwsOnTaskType() throws Exception {
            DriscollException excepted = new DriscollException(TaskExceptions.INVALID_TASK_TYPE.getStatus(), TaskExceptions.INVALID_TASK_TYPE.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTask(USER_ID, TASK_ID, TASK_REQUEST_INVALID_TYPE));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void validateTaskRequest_throwsOnStatusType() throws Exception {
            DriscollException excepted = new DriscollException(TaskExceptions.INVALID_STATUS.getStatus(), TaskExceptions.INVALID_STATUS.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTask(USER_ID, TASK_ID, TASK_REQUEST_INVALID_STATUS));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }

        @Test
        public void validTask_saveToRepository_goldenPath() throws Exception {
            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.of(TASK_DAO));
            when(taskRepository.save(TASK_DAO)).thenReturn(TASK_DAO);

            TaskDto actual = taskService.updateTask(USER_ID, TASK_ID, TASK_REQUEST);

            verify(taskRepository).findByUserIdAndTaskId(USER_ID, TASK_ID);
            verify(taskRepository).save(any(TaskDao.class));
        }

        @Test
        public void invalidTaskId_throw404Error() throws Exception {
            DriscollException excepted = new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());

            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());

            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTask(USER_ID, BAD_TASK_ID, TASK_REQUEST));

            assertEquals(excepted.getStatus(), actual.getStatus());
            assertEquals(excepted.getMessage(), actual.getMessage());
        }
    }
}