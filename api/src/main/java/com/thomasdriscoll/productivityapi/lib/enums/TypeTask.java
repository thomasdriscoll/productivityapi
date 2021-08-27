package com.thomasdriscoll.productivityapi.lib.enums;

public enum TypeTask {
    PHYSICAL("physical"),
    SPIRITUAL("spiritual"),
    EMOTIONAL("emotional"),
    INTELLECTUAL("intellectual");

    private final String type;

    TypeTask(String type){
        this.type = type;
    }
}
