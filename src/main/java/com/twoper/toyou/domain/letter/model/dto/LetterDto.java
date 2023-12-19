package com.twoper.toyou.domain.letter.model.dto;

import com.twoper.toyou.domain.letter.model.Letter;
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
