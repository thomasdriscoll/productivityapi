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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    // Main functions -- match order of methods with TaskController for organizational purposes
    public TaskDto createTask(String userId, TaskRequest newTaskRequest) throws DriscollException {
        TaskDto dto = validateTask(userId, newTaskRequest);
        taskRepository.save(new TaskDao(dto));
        return dto;
    }

    public TaskDto getTaskById(String userId, Long taskId) throws DriscollException {
        Optional<TaskDao> dao = taskRepository.findByUserIdAndTaskId(userId, taskId);
        if(dao.isEmpty()){
            throw new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());
        }
        return new TaskDto(dao.get());
    }

    public TaskDto updateTask(String userId, Long taskId, TaskRequest updateRequest) throws DriscollException {
        TaskDto dto = validateTask(userId, updateRequest);
        Optional<TaskDao> optionalTaskDao = taskRepository.findByUserIdAndTaskId(userId, taskId);
        if(optionalTaskDao.isEmpty()) {
            throw new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());
        }
        TaskDao dao = optionalTaskDao.get();
        dao.updateFromDto(dto);
        taskRepository.save(dao);
        return new TaskDto(dao);
    }

    public void deleteTask(String userId, Long taskId) throws DriscollException {
        try {
            taskRepository.deleteById(taskId);
        } catch (Exception ex) {
            throw new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());
        }

    }

    public TaskDto updateTaskStatus (String userId, Long taskId, String status) throws DriscollException {
        StatusType statusType = validateTaskStatus(status);
        Optional<TaskDao> optionalTaskDao = taskRepository.findByUserIdAndTaskId(userId, taskId);
        if(optionalTaskDao.isEmpty()){
            throw new DriscollException(TaskExceptions.TASK_ID_NOT_FOUND.getStatus(), TaskExceptions.TASK_ID_NOT_FOUND.getMessage());
        }
        TaskDto dto = new TaskDto(optionalTaskDao.get());
        dto.setStatusType(statusType);
        taskRepository.save(new TaskDao(dto));
        return dto;
    }

    public List<List<TaskDto>> getTasksOnBoard(String userId) throws DriscollException {
        List<List<TaskDto>> dtos = new ArrayList<>();
        // Contract: Order of statuses -- Backlog, To-Do, In-Progress, Blocked, Done, Archived
        // Board consists of To-Do, In-Progress, Blocked, Done
        List<TaskDao> toDo = taskRepository.findByUserIdAndStatusType(userId, StatusType.TODO);
        List<TaskDto> toDoDtos = convertListOfTaskDaoToTaskDto(toDo);
        dtos.add(toDoDtos);

        List<TaskDao> inProgress = taskRepository.findByUserIdAndStatusType(userId, StatusType.INPROGRESS);
        List<TaskDto> inProgressDtos = convertListOfTaskDaoToTaskDto(inProgress);
        dtos.add(inProgressDtos);

        List<TaskDao> blocked = taskRepository.findByUserIdAndStatusType(userId, StatusType.BLOCKED);
        List<TaskDto> blockedDtos = convertListOfTaskDaoToTaskDto(blocked);
        dtos.add(blockedDtos);

        List<TaskDao> done = taskRepository.findByUserIdAndStatusType(userId, StatusType.DONE);
        List<TaskDto> doneDtos = convertListOfTaskDaoToTaskDto(done);
        dtos.add(doneDtos);

        return dtos;
    }

    // Helper Functions
    private TaskDto validateTask(String userId, TaskRequest request) throws DriscollException {
        // check priorities
        PriorityTask priority = validateTaskPriority(request.getPriorityTask());
        StatusType statusType = validateTaskStatus(request.getStatusType());
        TypeTask typeTask = validateTypeTask(request.getTypeTask());

        return TaskDto.builder()
                .userId(userId)
                .titleTask(request.getTitleTask())
                .descriptionTask(request.getDescriptionTask())
                .estimatedTimeTask(request.getEstimatedTimeTask())
                .priorityTask(priority)
                .statusType(statusType)
                .typeTask(typeTask)
                .build();
    }

    private PriorityTask validateTaskPriority(String priority) throws DriscollException {
        PriorityTask priorityTask;
        try {
            priorityTask = PriorityTask.valueOf(priority);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DriscollException(HttpStatus.BAD_REQUEST, TaskExceptions.INVALID_TASK_PRIORITY.getMessage());
        }
        return priorityTask;
    }

    private StatusType validateTaskStatus(String status) throws DriscollException {
        StatusType statusType;
        try {
            statusType = StatusType.valueOf(status);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DriscollException(HttpStatus.BAD_REQUEST, TaskExceptions.INVALID_STATUS.getMessage());
        }
        return statusType;
    }

    private TypeTask validateTypeTask(String type) throws DriscollException {
        TypeTask typeTask;
        try {
            typeTask = TypeTask.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e){
            throw new DriscollException(HttpStatus.BAD_REQUEST, TaskExceptions.INVALID_TASK_TYPE.getMessage());
        }
        return typeTask;
    }

    private List<TaskDto> convertListOfTaskDaoToTaskDto(List<TaskDao> daos) {
        return daos.stream().map(TaskDto::new).toList();
    }


}
