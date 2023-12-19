package com.twoper.toyou.domain.letter.model.dto;

import lombok.Data;

@Data
public class RecipientSelectionDTO {
    private String username; // 보내는 사람
    private Long receiverId; // 받을 사람 ID
}
