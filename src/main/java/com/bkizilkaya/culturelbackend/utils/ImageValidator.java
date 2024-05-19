package com.bkizilkaya.culturelbackend.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@Component
public class ImageValidator {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; //10MB
    private static final Integer NO_HEIGHT = 0;
    private static final Integer NO_WIDTH = 0;

    public boolean isImage(MultipartFile file) {
        try {
            if (isFileSizeNull(file)) return false;
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            return checkDimensionsOfImage(image);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkDimensionsOfImage(BufferedImage image) {
        return image.getHeight() > NO_HEIGHT && image.getWidth() > NO_WIDTH;
    }

    public boolean isFileSizeValid(MultipartFile file) {
        if (isFileSizeNull(file)) return false;
        return file.getSize() <= MAX_FILE_SIZE;
    }

    private boolean isFileSizeNull(MultipartFile file) {
        return file == null || file.isEmpty();
    }
}
