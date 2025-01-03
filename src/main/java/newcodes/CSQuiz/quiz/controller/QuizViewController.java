package newcodes.CSQuiz.quiz.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.common.Category;
import newcodes.CSQuiz.quiz.domain.Page;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.Paging;
import newcodes.CSQuiz.quiz.dto.QuizSearchRequest;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;
import newcodes.CSQuiz.quiz.service.PagingService;
import newcodes.CSQuiz.quiz.service.QuizApiService;
import newcodes.CSQuiz.quiz.service.QuizViewService;
import newcodes.CSQuiz.user.dto.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class QuizViewController {

    private final QuizViewService quizViewService;
    private final QuizApiService quizApiService;
    private final PagingService pagingService;

    @GetMapping("/quizzes/{id}")
    public String showQuizDetails(@PathVariable int id,
                                  @AuthenticationPrincipal CustomUserDetails user,
                                  Model model) {
        Optional<Quiz> quizOptional = quizApiService.findById(id);

        if (quizOptional.isEmpty()) {
            model.addAttribute("error", "quizNotFound");
            return "quiz";
        }

        Integer userId = user != null ? user.getUserId() : null;
        QuizViewDTO quizViewDTO = quizViewService.createQuizViewDTO(quizOptional.get(), userId);

        model.addAttribute("quiz", quizViewDTO);
        model.addAttribute("loggedIn", user != null);

        return "quiz";
    }

    @GetMapping("/quizzes")
    public String showQuizzesWithSearch(@AuthenticationPrincipal CustomUserDetails user,
                                        @Valid QuizSearchRequest searchRequest,
                                        Model model) {
        int userId = Optional.ofNullable(user)
                .map(CustomUserDetails::getUserId)
                .orElse(0);

        List<QuizViewDTO> quizzes = quizApiService.findQuizzes(
                userId,
                searchRequest.getKeyword(),
                searchRequest.getCategories(),
                searchRequest.getStatusList()
        );

        Page<QuizViewDTO> pagedQuizzes = pagingService.getPage(
                quizzes,
                searchRequest.getPageNumber(),
                searchRequest.getPageSize()
        );

        model.addAttribute("quizzes", pagedQuizzes.getContent());
        model.addAttribute("paging", new Paging(pagedQuizzes.getPageNumber(), pagedQuizzes.getTotalPages()));
        model.addAttribute("categories", Category.getCategories());
        model.addAttribute("loggedIn", user != null);
        model.addAttribute("searchRequest", searchRequest);

        return "quizList";
    }

    @GetMapping("/new-quiz")
    public String showNewQuizForm(Model model) {
        model.addAttribute("quiz", Quiz.builder().build());
        model.addAttribute("categories", Category.getCategoriesWithIndex());

        return "newQuiz";
    }

    // TODO: 퀴즈 수정 View 제작
}
