package com.thomasdriscoll.productivityapi.lib.enums;

import lombok.Getter;

@Getter
public enum StatusType {
    BACKLOG ("backlog"),
    TODO("todo"),
    INPROGRESS ("in-progress"),
    BLOCKED ("blocked"),
    DONE ("done");

    private final String status;

    StatusType(String status){
        this.status = status;
    }
}
