package com.thomasdriscoll.productivityapi.lib.enums;

import lombok.Getter;

@Getter
public enum PriorityTask {
    HIGH ("HIGH"),
    MEDIUM ("MEDIUM"),
    LOW ("LOW");

    private final String priority;

    PriorityTask(String priority){
        this.priority = priority;
    }
}
