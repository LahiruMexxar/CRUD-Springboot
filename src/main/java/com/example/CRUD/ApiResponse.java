package com.example.CRUD;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {
    private int code;
    private  String message;
    private T payload;

    public ApiResponse(int code, String message, T payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    public ApiResponse() {

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

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public void setPayload(List<Book> existingBooks) {
    }
}
