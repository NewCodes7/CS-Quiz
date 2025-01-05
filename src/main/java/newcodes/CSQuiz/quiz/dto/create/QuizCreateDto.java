package newcodes.CSQuiz.quiz.dto.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;

@Getter
@Setter
@Builder
public class QuizCreateDto {
    private QuizDto quiz;
    private List<correctAndAlternativeAnswerDto> correctAndAlternativeAnswers;

    public Map<Answer, List<AlternativeAnswer>> getAnswerMap() {
        Map<Answer, List<AlternativeAnswer>> answerMap = new HashMap<>();

        for (correctAndAlternativeAnswerDto correctAndAlternativeAnswerDTO : correctAndAlternativeAnswers) {
            answerMap.put(correctAndAlternativeAnswerDTO.toAnswerEntity(),
                    correctAndAlternativeAnswerDTO.toAlternativeAnswerEntity());
        }

        return answerMap;
    }
}
