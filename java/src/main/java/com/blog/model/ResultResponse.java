package com.blog.model;

public class ResultResponse<T> {

    public Integer code;

    public String message;

    public T data;

    public ResultResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static ResultResponse success(String msg) {
        ResultResponse response =  new ResultResponse();
        response.code = 200;
        response.message = msg;
        response.data = "";
        return response;
    }

    public static <T> ResultResponse<T> success(String msg, T data) {
        return ResultResponse.success(msg).setData(data);
    }

    public static ResultResponse error(String msg) {
        ResultResponse response =  new ResultResponse();
        response.code = 400;
        response.message = msg;
        response.data = "";
        return response;
    }

    public static <T> ResultResponse<T> error(String msg, T data) {
        return ResultResponse.error(msg).setData(data);
    }
}
