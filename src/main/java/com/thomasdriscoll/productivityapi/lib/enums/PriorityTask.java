package com.thomasdriscoll.productivityapi.lib.enums;

import lombok.Getter;

@Getter
public enum PriorityTask {
    HIGH ("high"),
    MEDIUM ("medium"),
    LOW ("low");

    private final String priority;

    PriorityTask(String priority){
        this.priority = priority;
    }
}
