package newcodes.CSQuiz.quiz.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizSearchRequest {
    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
    private int pageNumber = 0;

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    private int pageSize = 10;

    @Size(max = 255, message = "검색 키워드는 255자를 넘을 수 없습니다.")
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