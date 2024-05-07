package newcodes.CSQuiz.quiz.controller;

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

@Controller
@RequiredArgsConstructor
public class QuizViewController {

    private final QuizService quizService;
    private final SubmissionService submissionService;

    @GetMapping("/quizzes")
    public String getQuizzes(Model model,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @RequestParam(defaultValue = "1") int pageNumber,
                             @RequestParam(defaultValue = "10") int pageSize,
                             @RequestParam(value = "kw", defaultValue = "") String kw,  // 타임리프와 매핑해보기 (학습)
                             @RequestParam(required = false) List<String> category) {
        List<QuizViewDTO> quizzes = quizService.findQuizzes(pageNumber, pageSize, kw, category);
        int totalPages = quizService.findAll().size(); // FIXME: 더 효율적으로 리팩토링 필요 -> JPA & Pageable
        Paging paging = new Paging(pageNumber, (int) Math.ceil((double) totalPages / pageSize));

        quizzes.forEach(quiz -> {
            Integer userId = customUserDetails.getUserId();
            Boolean isSolved = submissionService.findById(userId, quiz.getQuizId());
            quiz.setIsCorrect(isSolved);
        });

        model.addAttribute("quizzes", quizzes);
        model.addAttribute("kw", kw);
        model.addAttribute("paging", paging);
        model.addAttribute("categories", Category.getCategories());

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

        return "quiz";
    }

    private void handleQuizFound(Quiz quiz, CustomUserDetails customUserDetails, Model model) {
        int answerCounts = quizService.findAnswersById(quiz.getQuizId()).size();
        quiz.setAnswerCounts(answerCounts);

        boolean isCorrect = submissionService.findById(customUserDetails.getUserId(), quiz.getQuizId());
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

        return "newQuiz";
    }
}
