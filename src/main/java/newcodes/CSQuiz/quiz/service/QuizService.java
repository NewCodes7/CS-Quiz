package newcodes.CSQuiz.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.answer.dto.AnswerDTO;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import newcodes.CSQuiz.quiz.dto.QuizCreateRequest;
import newcodes.CSQuiz.quiz.dto.QuizUpdateRequest;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import newcodes.CSQuiz.quiz.repository.QuizUserRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizUserRequestRepository quizUserRequestRepository;

    @Transactional
    public Quiz saveQuizWithAnswers(QuizCreateRequest request) {
        Quiz quiz = request.getQuiz().toEntity();

        return quizRepository.save(quiz, createAnswerMap(request.getAnswers()));
    }

    @Transactional
    public Quiz createQuizFromRequest(Long id) {
        QuizUserRequest quizUserRequest = quizUserRequestRepository.findById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        QuizCreateRequest quizCreateRequest = null;

        try {
            quizCreateRequest = objectMapper.readValue(quizUserRequest.getRequestBody(), QuizCreateRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return quizRepository.save(quizCreateRequest.getQuiz().toEntity(),
                createAnswerMap(quizCreateRequest.getAnswers()));
    }

    private Map<Answer, List<AlternativeAnswer>> createAnswerMap(List<AnswerDTO> answerDTOs) {
        Map<Answer, List<AlternativeAnswer>> answerMap = new HashMap<>();

        for (AnswerDTO answerDTO : answerDTOs) {
            answerMap.put(answerDTO.toAnswerEntity(), answerDTO.toAlternativeAnswerEntity());
        }

        return answerMap;
    }

    public List<QuizViewDTO> findQuizzes(int userId, String kw, List<String> categories, List<String> statuses) {
        return quizRepository.findQuizzes(userId, kw, categories, statuses);
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

    @Transactional
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
