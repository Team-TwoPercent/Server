package com.example.twoper.jwt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import springfox.documentation.spring.web.json.Json;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class Response<T> {

    private String success;
    private String message;
    private T data;


}
