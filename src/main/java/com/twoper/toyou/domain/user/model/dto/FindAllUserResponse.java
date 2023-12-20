package com.twoper.toyou.domain.user.model.dto;

import com.twoper.toyou.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllUserResponse {
    private String success;

    private String message;

    private List<User> users;
}
