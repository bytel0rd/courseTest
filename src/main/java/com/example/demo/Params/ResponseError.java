package com.example.demo.Params;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@Builder
@Data
public class ResponseError {
    private ArrayList<Object> errors;
    private String message;
    private HttpStatus status;

}