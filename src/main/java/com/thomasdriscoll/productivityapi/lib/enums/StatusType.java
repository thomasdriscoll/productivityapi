package com.thomasdriscoll.productivityapi.lib.enums;

import lombok.Getter;

@Getter
public enum StatusType {
    BACKLOG ("BACKLOG"),
    TODO("TODO"),
    INPROGRESS ("INPROGRESS"),
    BLOCKED ("BLOCKED"),
    DONE ("DONE"),
    ARCHIVED("ARCHIVED");

    private final String status;

    StatusType(String status){
        this.status = status;
    }
}
