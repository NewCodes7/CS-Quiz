package newcodes.CSQuiz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.domain.Answer;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.AnswerDTO;
import newcodes.CSQuiz.dto.QuizDTO;
import newcodes.CSQuiz.dto.QuizRequest;
import newcodes.CSQuiz.repository.QuizRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public Quiz save(QuizRequest request) {
        Quiz quiz = request.getQuiz().toEntity();
        List<AnswerDTO> answerRequest = request.getAnswers();

        Map<Answer, List<AlternativeAnswer>> answers = new HashMap<>();

        for (AnswerDTO answer : answerRequest) {
            Answer mainAnswer = answer.toAnswerEntity();
            answers.put(mainAnswer, answer.toAlternativeAnswerEntity());
        }

        return quizRepository.save(quiz, answers);
    }

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> findById(int id) {
        return quizRepository.findById(id);
    }

    public void delete(int id) {
        quizRepository.delete(id);
    }
}
