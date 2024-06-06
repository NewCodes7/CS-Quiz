package newcodes.CSQuiz.global;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum Category {
    운영체제(1),
    네트워크(2),
    데이터베이스(3),
    자료구조(4),
    알고리즘(5),
    SW_신기술(6),
    정보처리기능사(7);

    Category(int id) {
        this.id = id;
    }

    @Getter
    private final int id;

    public static List<String> getCategories() {
        return Arrays.stream(Category.values())
                .map(Category::name)
                .collect(Collectors.toList());
    }
}
