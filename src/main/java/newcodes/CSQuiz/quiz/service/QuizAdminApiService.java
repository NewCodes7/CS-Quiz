package newcodes.CSQuiz.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.answer.dto.AnswerDTO;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import newcodes.CSQuiz.quiz.dto.QuizCreateRequest;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import newcodes.CSQuiz.quiz.repository.QuizUserRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizAdminApiService {

    private final QuizUserRequestRepository quizUserRequestRepository;
    private final QuizViewService quizViewService;
    private final QuizRepository quizRepository;

    @Transactional
    public Quiz approveAndSaveQuiz(Long id) {
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

    public List<QuizUserRequest> getPendingQuizRequests() {
        return quizUserRequestRepository.findByStatus("PENDING");
    }

    public QuizUserRequest approveQuizRequest(Long id) {
        QuizUserRequest quizRequest = quizUserRequestRepository.findById(id);
        if (quizRequest != null) {
            quizUserRequestRepository.updateStatus(id, "APPROVED");
            quizRequest.setStatus("APPROVED");
        }
        return quizRequest;
    }

    public QuizUserRequest rejectQuizRequest(Long id) {
        QuizUserRequest quizRequest = quizUserRequestRepository.findById(id);
        if (quizRequest != null) {
            quizUserRequestRepository.updateStatus(id, "REJECTED");
            quizRequest.setStatus("REJECTED");
        }
        return quizRequest;
    }
}
