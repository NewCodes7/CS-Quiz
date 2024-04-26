package newcodes.CSQuiz.controller;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.dto.QuizRequest;
import newcodes.CSQuiz.service.QuizService;
import newcodes.CSQuiz.domain.Quiz;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> addQuiz(@RequestBody QuizRequest request) {
        Quiz savedQuiz = quizService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedQuiz);
    }
}
