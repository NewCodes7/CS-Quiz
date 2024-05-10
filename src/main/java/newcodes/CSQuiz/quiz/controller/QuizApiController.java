package newcodes.CSQuiz.quiz.controller;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.dto.QuizCreateRequest;
import newcodes.CSQuiz.quiz.dto.QuizUpdateRequest;
import newcodes.CSQuiz.quiz.service.QuizService;
import newcodes.CSQuiz.quiz.domain.Quiz;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuizApiController {

    private final QuizService quizService;

    @PostMapping("/api/quizzes")
    public ResponseEntity<Quiz> addQuiz(@RequestBody QuizCreateRequest request) {
        Quiz savedQuiz = quizService.saveQuizWithAnswers(request);

        // FIXME: Quiz뿐만 아니라 Answer도 반환하기
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedQuiz);
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<List<Quiz>> findAllQuizzes() {
        List<Quiz> quizzes = quizService.findAll();

        return ResponseEntity.ok()
                .body(quizzes);
    }

    @GetMapping("/api/quizzes/{id}")
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

    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int id) {
        quizService.delete(id);

        // FIXME: 제대로 삭제되었는지 확인 응답 필요
        return ResponseEntity.ok()
                .build();
    }

    // TODO: 문제, 정답 업데이트 (QuizView와 연계)
    @PutMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable int id,
                                                 @RequestBody QuizUpdateRequest request) {
        Quiz updatedQuiz = quizService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedQuiz);
    }
}
