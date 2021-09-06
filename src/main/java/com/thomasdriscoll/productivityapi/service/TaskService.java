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

@Service
public class TaskService {
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public TaskDto createTask(String userId, TaskRequest newTaskRequest) throws DriscollException {
        TaskDto dto = validateTask(userId, newTaskRequest);
        TaskDao dao = taskRepository.save(new TaskDao(dto));
        return dto;
    }

    public TaskDto getTaskById(String userId, Long taskId) throws DriscollException {
        return null;
    }

    private TaskDto validateTask(String userId, TaskRequest request) throws DriscollException {
        // check priorities
        PriorityTask priority;
        try {
            priority = PriorityTask.valueOf(request.getPriorityTask());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DriscollException(HttpStatus.BAD_REQUEST, TaskExceptions.INVALID_TASK_PRIORITY.getMessage());
        }

        StatusType statusType;
        try {
            statusType = StatusType.valueOf(request.getStatusType());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DriscollException(HttpStatus.BAD_REQUEST, TaskExceptions.INVALID_STATUS.getMessage());
        }

        TypeTask typeTask;
        try {
            typeTask = TypeTask.valueOf(request.getTypeTask());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new DriscollException(HttpStatus.BAD_REQUEST, TaskExceptions.INVALID_TASK_TYPE.getMessage());
        }

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

}
