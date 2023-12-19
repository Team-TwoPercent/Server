package com.twoper.toyou.domain.letter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ZodiacSigne {
    RAT("Rat"),
    OX("Ox"),
    TIGER("Tiger"),
    RABBIT("Rabbit"),
    DRAGON("Dragon"),
    SNAKE("Snake"),
    HORSE("Horse"),
    GOAT("Goat"),
    MONKEY("Monkey"),
    ROOSTER("Rooster"),
    DOG("Dog"),
    PIG("Pig");

    private final String animal;
}
