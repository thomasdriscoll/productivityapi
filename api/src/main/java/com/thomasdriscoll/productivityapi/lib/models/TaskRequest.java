package com.thomasdriscoll.productivityapi.lib.models;

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
}
