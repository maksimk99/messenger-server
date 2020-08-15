package com.epam.messenger.service.impl;

import com.epam.messenger.service.FileWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class LocalFileWriter implements FileWriter {

    @Value("${image.absolute.path}")
    private String IMAGES_ABSOLUTE_PATH;

    @Value("${web.server.url}")
    private String WEB_SERVER_URL;

    public void deleteFile(final String imageUrl) {
        if (Objects.nonNull(imageUrl) && !imageUrl.isEmpty()) {
            String fileName = imageUrl.replaceFirst(WEB_SERVER_URL, "");
            File file = new File(IMAGES_ABSOLUTE_PATH + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public String writeFile(final MultipartFile file) {
        String fileName = UUID.randomUUID().toString();
        Path localFile = Paths.get(IMAGES_ABSOLUTE_PATH + fileName);
        try {
            Files.write(localFile, file.getBytes());
            if (Objects.equals(Files.size(localFile), file.getSize())) {
                return WEB_SERVER_URL + fileName;
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
