package newcodes.CSQuiz.quiz.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.answer.service.SubmissionService;
import newcodes.CSQuiz.global.Category;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.user.dto.CustomUserDetails;
import newcodes.CSQuiz.quiz.dto.Paging;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;
import newcodes.CSQuiz.quiz.service.QuizService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class QuizViewController {

    private final QuizService quizService;
    private final SubmissionService submissionService;

    @GetMapping("/quizzes")
    public String getQuizzes(Model model,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @RequestParam(defaultValue = "0") int pageNumber,
                             @RequestParam(defaultValue = "10") int pageSize,
                             @RequestParam(required = false, value = "kw") String kw,  // 타임리프와 매핑해보기 (학습)
                             @RequestParam(required = false) String category,
                             @RequestParam(required = false) String statuses) {
        // REFACTOR: 인자 클래스 DTO로 감싸기?
        if (category == null || category.isEmpty()) {
            category = "none";
        }
        if (statuses == null || statuses.isEmpty()) {
            statuses = "none";
        }

        List<String> categories = List.of(category.split(","));
        List<String> status = List.of(statuses.split(","));
        int userId = customUserDetails != null ? customUserDetails.getUserId() : 0;

        List<QuizViewDTO> quizzes = quizService.findQuizzes(userId, kw, categories, status);
        int totalElements = quizzes.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        if (totalPages == 0) totalPages = 1;

        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, totalElements);
        List<QuizViewDTO> pagedQuizzes = quizzes.subList(start, end);

        Paging paging = new Paging(pageNumber, totalPages);

        model.addAttribute("quizzes", pagedQuizzes);
        model.addAttribute("kw", kw);
        model.addAttribute("paging", paging);
        model.addAttribute("categories", Category.getCategories());
        model.addAttribute("loggedIn", customUserDetails != null);

        // 요청된 파라미터 그대로 버튼에 출력하기 위함
        model.addAttribute("category", category);
        model.addAttribute("statuses", statuses);

        return "quizList";
    }

    // NOTE: 한 요청 안에서 이렇게 메서드 나누는 게 일반적인가?
    @GetMapping("/quizzes/{id}")
    public String getQuiz(@PathVariable int id,
                          Model model,
                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        quizService.findById(id)
                .ifPresentOrElse(
                        quiz -> handleQuizFound(quiz, customUserDetails, model),
                        () -> handleQuizNotFound(model)
                );

        model.addAttribute("loggedIn", customUserDetails != null);

        return "quiz";
    }

    private void handleQuizFound(Quiz quiz, CustomUserDetails customUserDetails, Model model) {

        int answerCounts = quizService.findAnswersById(quiz.getQuizId()).size();
        quiz.setAnswerCounts(answerCounts);

        boolean isCorrect = false;
        if (customUserDetails != null) {
            isCorrect = submissionService.findById(customUserDetails.getUserId(), quiz.getQuizId());
        }
        QuizViewDTO quizViewDTO = new QuizViewDTO(quiz);
        quizViewDTO.setIsCorrect(isCorrect);

        model.addAttribute("quiz", quizViewDTO);
    }

    private void handleQuizNotFound(Model model) {
        model.addAttribute("error", "quizNotFound");
    }

    // TODO: quizId 통해서 퀴즈 수정 기능 구현 (QuizApiController 내 updateQuiz 메서드 이용해서)
    @GetMapping("/new-quiz")
    public String newQuiz(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {
            model.addAttribute("quiz", Quiz.builder().build());
        } else {
            Quiz quiz = quizService.findById(id).get(); // FIXME: Optional 예외 처리
            model.addAttribute("quiz", quiz);
        }

        model.addAttribute("categories", Category.getCategoriesWithIndex());

        return "newQuiz";
    }
}
