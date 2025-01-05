package newcodes.CSQuiz.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.domain.PendingQuiz;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.create.QuizCreateDto;
import newcodes.CSQuiz.quiz.repository.JdbcTemplatePendingQuizRepository;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizAdminService {

    private final JdbcTemplatePendingQuizRepository quizPendingRepository;
    private final QuizViewService quizViewService;
    private final QuizRepository quizRepository;

    @Transactional
    public Quiz approveAndSaveQuiz(Long id) {
        PendingQuiz pendingQuiz = quizPendingRepository.findPendingQuizById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        QuizCreateDto quizCreateDto = null;

        // REFACTOR: 공통 예외 처리 메서드로 분리
        try {
            quizCreateDto = objectMapper.readValue(pendingQuiz.getRequestBody(), QuizCreateDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return quizRepository.saveQuizAndAnswers(quizCreateDto);
    }

    public List<PendingQuiz> getPendingQuizRequests() {
        return quizPendingRepository.findPendingQuizByStatus("PENDING");
    }

    public PendingQuiz approveQuizRequest(Long id) {
        PendingQuiz quizRequest = quizPendingRepository.findPendingQuizById(id);
        if (quizRequest != null) {
            quizPendingRepository.updatePendingQuizStatus(id, "APPROVED");
            quizRequest.setStatus("APPROVED");
        }
        return quizRequest;
    }

    public PendingQuiz rejectQuizRequest(Long id) {
        PendingQuiz quizRequest = quizPendingRepository.findPendingQuizById(id);
        if (quizRequest != null) {
            quizPendingRepository.updatePendingQuizStatus(id, "REJECTED");
            quizRequest.setStatus("REJECTED");
        }
        return quizRequest;
    }
}
