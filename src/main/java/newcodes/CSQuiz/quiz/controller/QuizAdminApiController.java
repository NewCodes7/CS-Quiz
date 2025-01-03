package newcodes.CSQuiz.quiz.controller;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import newcodes.CSQuiz.quiz.service.QuizAdminApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes/requests")
public class QuizAdminApiController {

    private final QuizAdminApiService quizAdminApiService;

    @PutMapping("/{id}/approve")
    public ResponseEntity<QuizUserRequest> approveQuizRequest(@PathVariable Long id) {
        QuizUserRequest quizRequest = quizAdminApiService.approveQuizRequest(id);
        Quiz savedQuiz = quizAdminApiService.approveAndSaveQuiz(id);

        return new ResponseEntity<>(quizRequest, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<QuizUserRequest> rejectQuizRequest(@PathVariable Long id) {
        QuizUserRequest quizRequest = quizAdminApiService.rejectQuizRequest(id);
        return new ResponseEntity<>(quizRequest, HttpStatus.OK);
    }
}
