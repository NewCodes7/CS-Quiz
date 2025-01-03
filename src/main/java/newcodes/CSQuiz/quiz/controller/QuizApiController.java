package newcodes.CSQuiz.quiz.controller;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.domain.QuizUserRequest;
import newcodes.CSQuiz.quiz.dto.QuizUpdateRequest;
import newcodes.CSQuiz.quiz.service.QuizApiService;
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

    private final QuizApiService quizApiService;

    @PostMapping("/api/quizzes")
    public ResponseEntity<QuizUserRequest> createQuizRequest(@RequestBody String requestBody) {
        QuizUserRequest quizRequest = quizApiService.createQuizRequest(requestBody);
        return new ResponseEntity<>(quizRequest, HttpStatus.CREATED);
    }

    // TODO: 페이징 처리
    @GetMapping("/api/quizzes")
    public ResponseEntity<List<Quiz>> findAllQuizzes() {
        List<Quiz> quizzes = quizApiService.findAll();

        return ResponseEntity.ok()
                .body(quizzes);
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> findQuiz(@PathVariable int id) {
        Optional<Quiz> quiz = quizApiService.findById(id);

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
        quizApiService.delete(id);

        // FIXME: 제대로 삭제되었는지 확인 응답 필요
        return ResponseEntity.ok()
                .build();
    }

    // TODO: 문제, 정답 업데이트 (QuizView와 연계)
    @PutMapping("/api/quizzes/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable int id,
                                           @RequestBody QuizUpdateRequest request) {
        Quiz updatedQuiz = quizApiService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedQuiz);
    }
}
