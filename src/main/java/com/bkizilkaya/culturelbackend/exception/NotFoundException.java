package com.bkizilkaya.culturelbackend.exception;

import com.bkizilkaya.culturelbackend.model.FileData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private static final String NOT_FOUND = " not found";
    private static final String WITH_NAME = " with name: ";
    private String objectType;

    public NotFoundException(Class<?> clazz) {
        super(clazz.getSimpleName() + NOT_FOUND);
        objectType = clazz.getSimpleName();
    }

    public NotFoundException(Class<FileData> clazz, String fileName) {
        super(clazz.getSimpleName() + NOT_FOUND + WITH_NAME + fileName);
    }

    public String getObjectType() {
        return objectType;
    }
}
