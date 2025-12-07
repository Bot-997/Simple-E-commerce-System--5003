package com.example.backend.common;

import lombok.Data;

// common result for controller
@Data
public class Result<T> {
    private int code;      // 200 success, 500 failed
    private String msg;    // message
    private T data;        // data

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "success";
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 500;
        result.msg = msg;
        return result;
    }
}