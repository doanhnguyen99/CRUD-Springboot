package com.example.demo.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Component
public interface CSVService {
    ByteArrayInputStream load();
    void save(MultipartFile file);
}
