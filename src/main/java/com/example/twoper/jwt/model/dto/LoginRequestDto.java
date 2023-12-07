package com.example.twoper.jwt.model.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
