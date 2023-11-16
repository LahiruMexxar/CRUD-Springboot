package com.example.CRUD;

import java.util.ArrayList;

public class ApiResponse<T> {
    private int code;
    private  String message;
    private T payload;

    public ApiResponse(int code, String message, T payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.payload = null;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public T getPayload() {
        return payload;
    }
}
