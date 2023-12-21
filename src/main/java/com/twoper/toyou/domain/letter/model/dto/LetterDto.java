package com.twoper.toyou.domain.letter.model.dto;
import com.twoper.toyou.domain.letter.model.Letter;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LetterDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String content;
    private String username;
    private int receiverId;
    private String zodiacSign;
    private String  reseivername;

    public static LetterDto toDto (Letter letter){
        return new LetterDto(
                letter.getId(),
                letter.getTitle(),
                letter.getContent(),
                letter.getSender().getUsername(),
                letter.getReceiver().getId(),
                letter.getZodiacSing(),
                letter.getReceiver().getName()
                );

    }
    public String getUsername() {
        return this.username;
    }
}
