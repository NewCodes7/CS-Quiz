package newcodes.CSQuiz.quiz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.answer.dto.AnswerDTO;
import newcodes.CSQuiz.quiz.dto.QuizCreateRequest;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;
import newcodes.CSQuiz.quiz.dto.QuizUpdateRequest;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    @Transactional
    public Quiz saveQuizWithAnswers(QuizCreateRequest request) {
        Quiz quiz = request.getQuiz().toEntity();

        return quizRepository.save(quiz, createAnswerMap(request.getAnswers()));
    }

    private Map<Answer, List<AlternativeAnswer>> createAnswerMap(List<AnswerDTO> answerDTOs) {
        Map<Answer, List<AlternativeAnswer>> answerMap = new HashMap<>();

        for (AnswerDTO answerDTO : answerDTOs) {
            answerMap.put(answerDTO.toAnswerEntity(), answerDTO.toAlternativeAnswerEntity());
        }

        return answerMap;
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
    public Quiz update(int id, QuizUpdateRequest request) {
        Quiz quiz = request.getQuiz().toEntity();

        // FIXME: update 방식 리팩토링 (굳이 객체 update해서 보내줘야 하나?)
        //
        //     Quiz originalQuiz = quizRepository.findById(id)
        //           .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        //     Map<Answer, List<AlternativeAnswer>> originalAnswers = quizRepository.findAnswersById(id);
        // originalQuiz.update(quiz.getCategoryId(), quiz.getQuestionText(), quiz.getDifficulty(), quiz.getReferenceUrl(), quiz.getBlankSentence());

        return quizRepository.update(quiz, createAnswerMap(request.getAnswers()));
    }
}
