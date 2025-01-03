package newcodes.CSQuiz.quiz.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizSearchRequest {
    private int pageNumber = 0;
    private int pageSize = 10;
    private String keyword;
    private String category = "none";
    private String statuses = "none";

    public List<String> getCategories() {
        return List.of(category.split(","));
    }

    public List<String> getStatusList() {
        return List.of(statuses.split(","));
    }
}