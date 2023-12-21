package com.twoper.toyou.domain.letter;
import lombok.Getter;

@Getter
//@AllArgsConstructor
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

ZodiacSigne(String animal) {
    this.animal = animal;
}

    public String getAnimal() {
        return animal;
    }

    public ZodiacSigne getZodiacSign() {
        return this;
    }


    // 문자열을 Enum 값으로 변환하는 메서드 추가
    public static ZodiacSigne fromAnimal(String animal) {
        for (ZodiacSigne zodiacSigne : values()) {
            if (zodiacSigne.getAnimal().equalsIgnoreCase(animal)) {
                return zodiacSigne;
            }
        }
        throw new IllegalArgumentException("Invalid animal: " + animal);
    }

}
