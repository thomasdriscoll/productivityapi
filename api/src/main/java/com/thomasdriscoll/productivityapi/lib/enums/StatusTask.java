package com.thomasdriscoll.productivityapi.lib.enums;

public enum StatusTask {
    BACKLOG ("backlog"),
    TODO("todo"),
    INPROGRESS ("in-progress"),
    BLOCKED ("blocked"),
    DONE ("done");

    private final String status;

    StatusTask(String status){
        this.status = status;
    }
}
