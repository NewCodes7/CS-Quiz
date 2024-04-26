package newcodes.CSQuiz.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

        return quizRepository.save(quiz, answerRequest);
    }

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> findById(int id) {
        return quizRepository.findById(id);
    }
}
