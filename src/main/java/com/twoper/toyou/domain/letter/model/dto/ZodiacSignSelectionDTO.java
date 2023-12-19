package com.twoper.toyou.domain.letter.model.dto;

import com.twoper.toyou.domain.letter.ZodiacSigne;
import lombok.Data;


@Data
public class ZodiacSignSelectionDTO {
    private String username; // 사용자 아이디
    private ZodiacSigne zodiacSign;
}
