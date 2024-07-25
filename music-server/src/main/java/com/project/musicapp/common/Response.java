package com.project.musicapp.common;

import com.project.musicapp.constant.Constants;
import lombok.Data;

@Data
public class Response {
    private Integer code;

    private String message;

    private String type;

    private Boolean success;

    private Object data;

    public static Response success(String message) {
        Response response = new Response();
        response.setCode(200);
        response.setMessage(message);
        response.setType(Constants.SUCCESS);
        response.setSuccess(true);
        response.setData(null);
        return response;
    }

    public static Response success(String message, Object data) {
        Response response = success(message);
        response.setData(data);
        return response;
    }

    public static Response error(String message) {
        Response response = success(message);
        response.setCode(500);
        response.setType(Constants.ERROR);
        response.setSuccess(false);
        return response;
    }

    public static Response warning(String message) {
        Response response = error(message);
        response.setCode(400);
        response.setType(Constants.BAD_REQUEST);
        return response;
    }

    public static Response notFound(String message) {
        Response response = error(message);
        response.setCode(404);
        response.setType(Constants.NOT_FOUND);
        return response;
    }
}
