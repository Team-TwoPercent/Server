package com.twoper.toyou.domain.user.model.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
