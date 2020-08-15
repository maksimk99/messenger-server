package com.epam.messenger.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileWriter {

    void deleteFile(String fileName);

    String writeFile(MultipartFile file);
}