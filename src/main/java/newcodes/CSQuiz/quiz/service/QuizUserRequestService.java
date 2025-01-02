package newcodes.CSQuiz.quiz.service;

import java.time.LocalDateTime;
import java.util.List;
import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import newcodes.CSQuiz.quiz.repository.QuizUserRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizUserRequestService {

    @Autowired
    private QuizUserRequestRepository quizRequestRepository;
    @Autowired
    private QuizService quizService;

    public QuizUserRequest createQuizRequest(String requestBody) {
        QuizUserRequest quizRequest = new QuizUserRequest();
        quizRequest.setRequestBody(requestBody);
        quizRequest.setStatus("PENDING");
        quizRequest.setCreatedAt(LocalDateTime.now());
        quizRequest.setUpdatedAt(LocalDateTime.now());
        quizRequestRepository.save(quizRequest);
        return quizRequest;
    }

    public List<QuizUserRequest> getPendingQuizRequests() {
        return quizRequestRepository.findByStatus("PENDING");
    }

    public QuizUserRequest approveQuizRequest(Long id) {
        QuizUserRequest quizRequest = quizRequestRepository.findById(id);
        if (quizRequest != null) {
            quizRequestRepository.updateStatus(id, "APPROVED");
            quizRequest.setStatus("APPROVED");
        }
        return quizRequest;
    }

    public QuizUserRequest rejectQuizRequest(Long id) {
        QuizUserRequest quizRequest = quizRequestRepository.findById(id);
        if (quizRequest != null) {
            quizRequestRepository.updateStatus(id, "REJECTED");
            quizRequest.setStatus("REJECTED");
        }
        return quizRequest;
    }
}
