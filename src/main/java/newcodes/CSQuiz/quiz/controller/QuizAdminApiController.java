package newcodes.CSQuiz.quiz.controller;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.domain.PendingQuiz;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.service.QuizAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes/status/pending")
public class QuizAdminApiController {

    private final QuizAdminService quizAdminService;

    @PutMapping("/{id}/approve")
    public ResponseEntity<PendingQuiz> approveQuizRequest(@PathVariable Long id) {
        PendingQuiz quizRequest = quizAdminService.approveQuizRequest(id);
        Quiz savedQuiz = quizAdminService.approveAndSaveQuiz(id);

        return new ResponseEntity<>(quizRequest, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PendingQuiz> rejectQuizRequest(@PathVariable Long id) {
        PendingQuiz quizRequest = quizAdminService.rejectQuizRequest(id);
        return new ResponseEntity<>(quizRequest, HttpStatus.OK);
    }
}
