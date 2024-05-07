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
import newcodes.CSQuiz.dto.QuizViewDTO;
import newcodes.CSQuiz.dto.UpdateQuizRequest;
import newcodes.CSQuiz.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<QuizViewDTO> findQuizzes(int pageNumber, int pageSize, String kw, List<String> categories) {
        return quizRepository.findQuizzes(pageNumber, pageSize, kw, categories);
    }

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> findById(int id) {
        return quizRepository.findById(id);
    }

    public Map<Answer, List<AlternativeAnswer>> findAnswersById(int id) {
        return quizRepository.findAnswersById(id);
    }

    public void delete(int id) {
        quizRepository.delete(id);
    }

    @Transactional // 역할?
    public Quiz update(int id, UpdateQuizRequest request) {
        Quiz quiz = request.getQuiz().toEntity();
        List<AnswerDTO> answerRequest = request.getAnswers();

        Map<Answer, List<AlternativeAnswer>> answers = new HashMap<>();

        for (AnswerDTO answer : answerRequest) {
            Answer mainAnswer = answer.toAnswerEntity();
            answers.put(mainAnswer, answer.toAlternativeAnswerEntity());
        }

        // FIXME: update 방식 리팩토링
            // 굳이 객체 update해서 보내줘야 하나?
            //     Quiz originalQuiz = quizRepository.findById(id)
            //           .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
            //     Map<Answer, List<AlternativeAnswer>> originalAnswers = quizRepository.findAnswersById(id);
            // originalQuiz.update(quiz.getCategoryId(), quiz.getQuestionText(), quiz.getDifficulty(), quiz.getReferenceUrl(), quiz.getBlankSentence());

        quizRepository.update(quiz, answers);

        return quiz;
    }
}
