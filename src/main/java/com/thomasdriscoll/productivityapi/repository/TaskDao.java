package com.thomasdriscoll.productivityapi.repository;

import com.thomasdriscoll.productivityapi.lib.enums.PriorityTask;
import com.thomasdriscoll.productivityapi.lib.enums.StatusType;
import com.thomasdriscoll.productivityapi.lib.enums.TypeTask;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;

import javax.persistence.*;

@Entity
@Table(name="t_tasks")
public class TaskDao {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Long taskId = null;

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

    public TaskDto toDto(){
        return null;
    }

    public TaskDao(
            Long taskId,
            String userId,
            String titleTask,
            String descriptionTask,
            PriorityTask priorityTask,
            Integer estimatedTimeTask,
            TypeTask typeTask,
            StatusType statusType
    ) {
        this.taskId = taskId;
        this.userId = userId;
        this.titleTask = titleTask;
        this.descriptionTask = descriptionTask;
        this.priorityTask = priorityTask;
        this.estimatedTimeTask = estimatedTimeTask;
        this.typeTask = typeTask;
        this.statusType = statusType;
    }
}
