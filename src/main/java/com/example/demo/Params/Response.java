package com.example.demo.Params;

import org.springframework.http.HttpStatus;

public class Response<T> {

    private T data;
    private HttpStatus statusCode;

    public Response(T data) {
        this.data = data;
        this.statusCode = HttpStatus.OK;
    }

    public Response(T data, HttpStatus statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }
}
