package com.example.twoper.Letter.model.dto;

import lombok.Data;

@Data
public class RecipientSelectionDTO {
    private String senderName; // 보내는 사람 ID
    private Long receiverId; // 받을 사람 ID
}
