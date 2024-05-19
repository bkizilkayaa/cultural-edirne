package com.bkizilkaya.culturelbackend.model;

import lombok.Getter;

@Getter
public enum TableNameEnum {

    ARTWORKS("ARTWORKS"),
    TOURIST_SPOT("TOURIST SPOT"),
    AUTHORS("AUTHORS"),
    ZIP_CODES("ZIP_CODES"),
    FILE_DATA("FILE_DATA"),
    ARTWORK_IMAGES("ARTWORK_IMAGES"),
    TOURIST_SPOT_IMAGES("TOURIST_SPOT_IMAGE");

    private final String tableName;

    private TableNameEnum(String tableName) {
        this.tableName = tableName;
    }


}
