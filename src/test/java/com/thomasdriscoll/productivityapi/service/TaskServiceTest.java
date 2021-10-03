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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
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
        public void validateTaskRequest_throwsOnPriority() {
            DriscollException expected = new DriscollException(TaskExceptions.INVALID_TASK_PRIORITY.getStatus(), TaskExceptions.INVALID_TASK_PRIORITY.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.createTask(USER_ID, TASK_REQUEST_INVALID_PRIORITY));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
        }

        @Test
        public void validateTaskRequest_throwsOnTaskType() {
            DriscollException expected = new DriscollException(TaskExceptions.INVALID_TASK_TYPE.getStatus(), TaskExceptions.INVALID_TASK_TYPE.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.createTask(USER_ID, TASK_REQUEST_INVALID_TYPE));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
        }

        @Test
        public void validateTaskRequest_throwsOnStatusType() {
            DriscollException expected = new DriscollException(TaskExceptions.INVALID_STATUS.getStatus(), TaskExceptions.INVALID_STATUS.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.createTask(USER_ID, TASK_REQUEST_INVALID_STATUS));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
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
        public void invalidTaskId_throw404Error() {
            DriscollException expected = new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());

            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());

            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.getTaskById(USER_ID, BAD_TASK_ID));

            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
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
        public void validateTaskRequest_throwsOnPriority() {
            DriscollException expected = new DriscollException(TaskExceptions.INVALID_TASK_PRIORITY.getStatus(), TaskExceptions.INVALID_TASK_PRIORITY.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTask(USER_ID, TASK_ID, TASK_REQUEST_INVALID_PRIORITY));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
        }

        @Test
        public void validateTaskRequest_throwsOnTaskType() {
            DriscollException expected = new DriscollException(TaskExceptions.INVALID_TASK_TYPE.getStatus(), TaskExceptions.INVALID_TASK_TYPE.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTask(USER_ID, TASK_ID, TASK_REQUEST_INVALID_TYPE));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
        }

        @Test
        public void validateTaskRequest_throwsOnStatusType() {
            DriscollException expected = new DriscollException(TaskExceptions.INVALID_STATUS.getStatus(), TaskExceptions.INVALID_STATUS.getMessage());
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTask(USER_ID, TASK_ID, TASK_REQUEST_INVALID_STATUS));

            // Note: AssertEquals does a deep assertion, i.e. it is testing if the objects are literally the same object in memory. Easiest way around this is to test contents
            // Good enough for our purposes
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
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
        public void invalidTaskId_throw404Error() {
            DriscollException expected = new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());

            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());

            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTask(USER_ID, BAD_TASK_ID, TASK_REQUEST));

            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
        }
    }

    @Nested
    @DisplayName("Delete Task Service tests")
    class DeleteTaskServiceTests {
        @Test
        public void validTaskId_thenDelete() throws Exception {
            taskService.deleteTask(USER_ID, TASK_ID);
            verify(taskRepository).deleteById(TASK_ID);
        }

        @Test
        public void invalidTaskId_throw404Error() {
            DriscollException expected = new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());

            doThrow(IllegalArgumentException.class).when(taskRepository).deleteById(BAD_TASK_ID);

            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.deleteTask(USER_ID, BAD_TASK_ID));

            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
        }
    }
    
    @Nested
    @DisplayName("Update Task Status Tests")
    class UpdateTaskStatusTests {
        @Test
        public void validTaskId_thenUpdateStatus_AndReturnDto() throws DriscollException {
            TaskDto expected = TaskDto.builder()
                    .userId(USER_ID)
                    .titleTask(TITLE_TASK)
                    .descriptionTask(DESCRIPTION_TASK)
                    .priorityTask(PriorityTask.valueOf(PRIORITY_TASK))
                    .estimatedTimeTask(ESTIMATED_TIME_TASK)
                    .statusType(StatusType.valueOf(StatusType.TODO.getStatus()))
                    .typeTask(TypeTask.valueOf(TASK_TYPE))
                .build();
            TaskDao expectedDao = new TaskDao(expected);
            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.of(TASK_DAO));
            when(taskRepository.save(any(TaskDao.class))).thenReturn(expectedDao);
            TaskDto actual = taskService.updateTaskStatus(USER_ID, TASK_ID, StatusType.TODO.getStatus());
            verify(taskRepository).save(any(TaskDao.class));
            assertEquals(expected, actual);
        }

        @Test
        public void invalidStatus_thenThrow400() {
            DriscollException expected = new DriscollException(TaskExceptions.INVALID_STATUS.getStatus(), TaskExceptions.INVALID_STATUS.getMessage());
            
            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTaskStatus(USER_ID, TASK_ID, "junk"));
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
        }

        @Test
        public void invalidTaskId_thenThrow404() {
            DriscollException expected = new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());

            when(taskRepository.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());

            DriscollException actual = assertThrows(DriscollException.class, () -> taskService.updateTaskStatus(USER_ID, BAD_TASK_ID, STATUS_TYPE));

            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getMessage(), actual.getMessage());
        }
    }

    @Nested
    @DisplayName("Get Tasks On Board Tests")
    class GetTasksOnBoardTests {
        TaskDao toDoDao = new TaskDao(TASK_DAO);
        TaskDao inProgressDao = new TaskDao(TASK_DAO);
        TaskDao blockedDao = new TaskDao(TASK_DAO);
        TaskDao doneDao = new TaskDao(TASK_DAO);
        List<TaskDao> daos;
        List<TaskDto> dtos;

        @BeforeEach
        public void setup() {
            // Set up variables
            toDoDao.setStatusType(StatusType.TODO);
            inProgressDao.setStatusType(StatusType.INPROGRESS);
            blockedDao.setStatusType(StatusType.BLOCKED);
            doneDao.setStatusType(StatusType.DONE);
        }

        @Test
        public void getTasksOnBoard_returnToDoType() throws DriscollException {
            daos = List.of(toDoDao);
            dtos = (List<TaskDto>) daos.stream().map(TaskDto::new);

            when(taskRepository.findByUserIdAndStatusType(USER_ID, StatusType.TODO)).thenReturn(daos);

            List<TaskDto> actualDtos = taskService.getTasksOnBoard(USER_ID);

            assertEquals(1, dtos.size());
            assertEquals(dtos.get(0).getStatusType(), actualDtos.get(0).getStatusType());
        }

        @Test
        public void getTasksOnBoard_returnsInProgressType() throws DriscollException {
            daos = List.of(inProgressDao);
            dtos = (List<TaskDto>) daos.stream().map(TaskDto::new);

            when(taskRepository.findByUserIdAndStatusType(USER_ID, StatusType.INPROGRESS)).thenReturn(daos);

            List<TaskDto> actualDtos = taskService.getTasksOnBoard(USER_ID);

            assertEquals(1, dtos.size());
            assertEquals(dtos.get(0).getStatusType(), actualDtos.get(0).getStatusType());
        }

        @Test
        public void getTasksOnBoard_returnBlocked() throws DriscollException {
            daos = List.of(blockedDao);
            dtos = (List<TaskDto>) daos.stream().map(TaskDto::new);

            when(taskRepository.findByUserIdAndStatusType(USER_ID, StatusType.BLOCKED)).thenReturn(daos);

            List<TaskDto> actualDtos = taskService.getTasksOnBoard(USER_ID);

            assertEquals(1, dtos.size());
            assertEquals(dtos.get(0).getStatusType(), actualDtos.get(0).getStatusType());
        }

        @Test
        public void getTasksOnBoard_returnDone() throws DriscollException {
            daos = List.of(doneDao);
            dtos = (List<TaskDto>) daos.stream().map(TaskDto::new);

            when(taskRepository.findByUserIdAndStatusType(USER_ID, StatusType.DONE)).thenReturn(daos);

            List<TaskDto> actualDtos = taskService.getTasksOnBoard(USER_ID);

            assertEquals(1, dtos.size());
            assertEquals(dtos.get(0).getStatusType(), actualDtos.get(0).getStatusType());
        }

        @Test
        public void getTasksOnBoard_returnTasksOfAllTypes() throws DriscollException {
            daos = List.of(TASK_DAO, inProgressDao, toDoDao, blockedDao);
            dtos = (List<TaskDto>) daos.stream().map(TaskDto::new);

            when(taskRepository.findByUserIdAndStatusType(USER_ID, StatusType.TODO, StatusType.INPROGRESS, StatusType.BLOCKED, StatusType.DONE)).thenReturn(daos);

            List<TaskDto> actualDtos = taskService.getTasksOnBoard(USER_ID);

            assertEquals(4, dtos.size());
            assertEquals(dtos.get(0).getStatusType(), actualDtos.get(0).getStatusType());
            assertEquals(dtos.get(1).getStatusType(), actualDtos.get(1).getStatusType());
            assertEquals(dtos.get(2).getStatusType(), actualDtos.get(2).getStatusType());
            assertEquals(dtos.get(3).getStatusType(), actualDtos.get(3).getStatusType());

        }

        @Test
        public void getTasksOnBoard_returnNoTasks() throws DriscollException {
            when(taskRepository.findByUserIdAndStatusType(USER_ID, StatusType.TODO, StatusType.INPROGRESS, StatusType.BLOCKED, StatusType.DONE)).thenReturn(List.of());

            List<TaskDto> actualDtos = taskService.getTasksOnBoard(USER_ID);

            assertEquals(0, dtos.size());
        }
    }
}