package com.example.twoper.Letter.model.dto;

import com.example.twoper.Letter.model.Letter;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LetterDto {

    private int id;
    private String title;
    private String content;
    private String senderName;
    private String receiverName;


    public static LetterDto toDto (Letter letter){
        return new LetterDto(
                letter.getId(),
                letter.getTitle(),
                letter.getContent(),
                letter.getSender().getName(),
                letter.getReceiver().getName()
        );
    }
}
