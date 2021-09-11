package com.thomasdriscoll.productivityapi.repository;

import com.thomasdriscoll.productivityapi.lib.enums.PriorityTask;
import com.thomasdriscoll.productivityapi.lib.enums.StatusType;
import com.thomasdriscoll.productivityapi.lib.enums.TypeTask;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="t_tasks")
@Getter
public class TaskDao {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Long taskId;

    @Column(name="user_id")
    private String userId;

    @Column(name="title_task")
    private String titleTask;

    @Column(name="description_task")
    private String descriptionTask;

    @Column(name="priority_task")
    @Enumerated(EnumType.STRING)
    private PriorityTask priorityTask;

    @Column(name="estimated_time_task")
    private Integer estimatedTimeTask;

    @Column(name="type_task")
    @Enumerated(EnumType.STRING)
    private TypeTask typeTask;

    @Column(name="status_task")
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    public TaskDao(TaskDto dto) {
        this.userId = dto.getUserId();
        this.titleTask = dto.getTitleTask();
        this.descriptionTask = dto.getDescriptionTask();
        this.priorityTask = dto.getPriorityTask();
        this.estimatedTimeTask = dto.getEstimatedTimeTask();
        this.typeTask = dto.getTypeTask();
        this.statusType = dto.getStatusType();
    }

    public void updateFromDto(TaskDto dto){
        this.userId = dto.getUserId();
        this.titleTask = dto.getTitleTask();
        this.descriptionTask = dto.getDescriptionTask();
        this.priorityTask = dto.getPriorityTask();
        this.estimatedTimeTask = dto.getEstimatedTimeTask();
        this.typeTask = dto.getTypeTask();
        this.statusType = dto.getStatusType();
    }
}
