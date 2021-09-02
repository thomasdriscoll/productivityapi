package com.thomasdriscoll.productivityapi.lib.models;

import com.thomasdriscoll.productivityapi.lib.enums.PriorityTask;
import com.thomasdriscoll.productivityapi.lib.enums.StatusType;
import com.thomasdriscoll.productivityapi.lib.enums.TypeTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TaskDto {
    private String userId;
    private String titleTask;
    private String descriptionTask;
    private PriorityTask priorityTask;
    private Integer estimatedTimeTask;
    private TypeTask typeTask;
    private StatusType statusType;

    public TaskDto() {}

    public TaskDto (String userId, TaskRequest taskRequest) {
        this.userId = userId;
        this.titleTask = taskRequest.titleTask;
        this.descriptionTask = taskRequest.descriptionTask;
        this.priorityTask = PriorityTask.valueOf(taskRequest.getPriorityTask());
        this.estimatedTimeTask = taskRequest.estimatedTimeTask;
        this.typeTask = TypeTask.valueOf(taskRequest.getTypeTask());
        this.statusType = StatusType.valueOf(taskRequest.getStatusType());
    }
}
