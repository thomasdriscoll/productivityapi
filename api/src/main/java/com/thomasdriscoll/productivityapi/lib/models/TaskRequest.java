package com.thomasdriscoll.productivityapi.lib.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize
public class TaskRequest {
    String titleTask;
    String descriptionTask;
    String priorityTask;
    Integer estimatedTimeTask;
    String typeTask;
    String statusType;

    public TaskRequest(
            @JsonProperty("titleTask") String titleTask,
            @JsonProperty("descriptionTask") String descriptionTask,
            @JsonProperty("priorityTask") String priorityTask,
            @JsonProperty("estimatedTimeTask") Integer estimatedTimeTask,
            @JsonProperty("typeTask") String typeTask,
            @JsonProperty("statusType") String statusType
    ) {
        this.titleTask = titleTask;
        this.descriptionTask = descriptionTask;
        this.priorityTask = priorityTask;
        this.estimatedTimeTask = estimatedTimeTask;
        this.typeTask = typeTask;
        this.statusType = statusType;
    }
}
