package newcodes.CSQuiz.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    private int number;
    private int totalPages;
    private boolean hasPrevious;
    private boolean hasNext;

    public Paging(int number, int totalPages) {
        this.number = number;
        this.totalPages = totalPages;

        if (number == totalPages - 1) {
            hasNext = false;
        } else {
            hasNext = true;
        }

        if (number == 0) {
            hasPrevious = false;
        } else {
            hasPrevious = true;
        }
    }

    public boolean isEmpty() {
        return false;
    }
}
