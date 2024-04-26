package newcodes.CSQuiz.controller;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.dto.QuizRequest;
import newcodes.CSQuiz.service.QuizService;
import newcodes.CSQuiz.domain.Quiz;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> addQuiz(@RequestBody QuizRequest request) {
        Quiz savedQuiz = quizService.save(request);

        // FIXME: Quiz뿐만 아니라 Answer도 반환하기
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedQuiz);
    }

    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> findAllQuizzes() {
        List<Quiz> quizzes = quizService.findAll();

        return ResponseEntity.ok()
                .body(quizzes);
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> findQuiz(@PathVariable int id) {
        Optional<Quiz> quiz = quizService.findById(id);

        if (quiz.isPresent()) {
            return ResponseEntity.ok()
                    .body(quiz.get());
        } else {
            // FIXME: 404 에러 뜰 수 있게 ResponseDTO 설정 필요
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(quiz.get());
        }
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int id) {
        quizService.delete(id);

        // FIXME: 제대로 삭제되었는지 확인 응답 필요
        return ResponseEntity.ok()
                .build();
    }
}
