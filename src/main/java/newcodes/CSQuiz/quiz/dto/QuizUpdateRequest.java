package newcodes.CSQuiz.quiz.dto;

import java.util.List;
import lombok.Getter;
import newcodes.CSQuiz.quiz.dto.create.QuizDto;
import newcodes.CSQuiz.quiz.dto.create.correctAndAlternativeAnswerDto;

@Getter
public class QuizUpdateRequest {
    private QuizDto quiz;
    private List<correctAndAlternativeAnswerDto> answers;
}
