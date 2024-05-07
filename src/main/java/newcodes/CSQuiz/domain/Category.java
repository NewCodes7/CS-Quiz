package newcodes.CSQuiz.domain;

import lombok.Getter;

public enum Category {
    운영체제(1),
    네트워크(2),
    데이터베이스(3),
    자료구조(4),
    알고리즘(5);

    @Getter
    private final int id;

    Category(int id) {
        this.id = id;
    }
}
