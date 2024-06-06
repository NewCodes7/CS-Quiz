package newcodes.CSQuiz.quiz.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import newcodes.CSQuiz.quiz.dto.QuizCreateRequest;
import newcodes.CSQuiz.quiz.service.QuizService;
import newcodes.CSQuiz.quiz.service.QuizUserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz-requests")
@Slf4j
public class QuizUserRequestController {

    @Autowired
    private QuizUserRequestService quizRequestService;

    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizUserRequest> createQuizRequest(@RequestBody String requestBody) {
        log.info("퀴즈 생성 요청");

        QuizUserRequest quizRequest = quizRequestService.createQuizRequest(requestBody);
        return new ResponseEntity<>(quizRequest, HttpStatus.CREATED);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<QuizUserRequest>> getPendingQuizRequests() {
        List<QuizUserRequest> quizRequests = quizRequestService.getPendingQuizRequests();
        return new ResponseEntity<>(quizRequests, HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<QuizUserRequest> approveQuizRequest(@PathVariable Long id) {
        QuizUserRequest quizRequest = quizRequestService.approveQuizRequest(id);
        Quiz savedQuiz = quizService.createQuizFromRequest(id);

        return new ResponseEntity<>(quizRequest, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<QuizUserRequest> rejectQuizRequest(@PathVariable Long id) {
        QuizUserRequest quizRequest = quizRequestService.rejectQuizRequest(id);
        return new ResponseEntity<>(quizRequest, HttpStatus.OK);
    }
}
