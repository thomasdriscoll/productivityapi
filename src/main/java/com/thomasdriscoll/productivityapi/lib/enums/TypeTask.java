package com.thomasdriscoll.productivityapi.lib.enums;

import lombok.Getter;

@Getter
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
