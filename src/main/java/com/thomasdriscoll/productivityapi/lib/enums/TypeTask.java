package com.thomasdriscoll.productivityapi.lib.enums;

import lombok.Getter;

@Getter
public enum TypeTask {
    PHYSICAL("PHYSICAL"),
    SPIRITUAL("SPIRITUAL"),
    EMOTIONAL("EMOTIONAL"),
    INTELLECTUAL("INTELLECTUAL");

    private final String type;

    TypeTask(String type){
        this.type = type;
    }
}
