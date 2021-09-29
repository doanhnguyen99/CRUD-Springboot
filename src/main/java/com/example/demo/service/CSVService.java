package com.example.demo.service;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public interface CSVService {
    ByteArrayInputStream load();
}
