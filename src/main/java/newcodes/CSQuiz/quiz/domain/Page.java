package newcodes.CSQuiz.quiz.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int totalPages;
    private final int totalElements;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public Page(List<T> content, int pageNumber, int totalPages, int totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = pageNumber < totalPages - 1;
        this.hasPrevious = pageNumber > 0;
    }
}