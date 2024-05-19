package com.bkizilkaya.culturelbackend.model;

import lombok.Getter;

@Getter
public enum ActionEnum {

    LOGIN("GIRIS"),
    ADD("EKLEME"),
    GET("LISTELEME"),
    UPDATE("GUNCELLEME"),
    DELETE("SILME");

    private final String action;

    private ActionEnum(String action) {
        this.action = action;
    }
}
