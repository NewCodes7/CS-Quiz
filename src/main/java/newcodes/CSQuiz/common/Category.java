package newcodes.CSQuiz.common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

public enum Category {
    운영체제(1),
    네트워크(2),
    데이터베이스(3),
    자료구조(4),
    알고리즘(5),
    SW_신기술(6),
    정보처리기사(7);

    @Getter
    private final int id;

    Category(int id) {
        this.id = id;
    }

    public static List<String> getCategories() {
        return Arrays.stream(Category.values())
                .map(Category::name)
                .collect(Collectors.toList());
    }

    public static Map<Integer, String> getCategoriesWithIndex() {
        return Arrays.stream(Category.values())
                .collect(Collectors.toMap(Category::getId, Category::name));
    }
}
