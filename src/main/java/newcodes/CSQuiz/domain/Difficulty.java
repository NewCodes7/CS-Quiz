package newcodes.CSQuiz.domain;

import lombok.Getter;

@Getter
public enum Difficulty {
    BRONZE("브론즈"),
    SILVER("실버"),
    GOLD("골드"),
    PLATINUM("플래티넘");

    private final String value;

    Difficulty(String value) {
        this.value = value;
    }
}

