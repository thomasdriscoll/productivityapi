package com.thomasdriscoll.productivityapi.lib.models;

import com.thomasdriscoll.productivityapi.lib.enums.PriorityTask;
import com.thomasdriscoll.productivityapi.lib.enums.StatusTask;
import com.thomasdriscoll.productivityapi.lib.enums.TypeTask;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {
    private String titleTask;
    private String descriptionTask;
    private PriorityTask priorityTask;
    private Integer estimatedTimeTask;
    private TypeTask typeTask;
    private StatusTask statusTask;
}
