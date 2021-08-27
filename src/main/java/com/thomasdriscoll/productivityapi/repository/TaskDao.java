package com.thomasdriscoll.productivityapi.repository;

import com.thomasdriscoll.productivityapi.lib.enums.PriorityTask;
import com.thomasdriscoll.productivityapi.lib.enums.StatusTask;
import com.thomasdriscoll.productivityapi.lib.enums.TypeTask;
import com.thomasdriscoll.productivityapi.lib.models.TaskDto;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="t_tasks")
public class TaskDao {
    @Id
    @GeneratedValue
    @Column(name="id")
    private final Long taskId;

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
    private StatusTask statusTask;

    public TaskDto toDto(){
        return null;
    }
}
