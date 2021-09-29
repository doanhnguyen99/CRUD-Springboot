package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Response {
    private final HttpStatus httpStatus;
    private final String message;
}
