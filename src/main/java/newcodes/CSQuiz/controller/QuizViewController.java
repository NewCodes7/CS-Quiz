package newcodes.CSQuiz.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.CustomUserDetails;
import newcodes.CSQuiz.dto.Paging;
import newcodes.CSQuiz.dto.QuizViewDTO;
import newcodes.CSQuiz.service.QuizService;
import newcodes.CSQuiz.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
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
                             @RequestParam(defaultValue = "10") int pageSize) {
        List<QuizViewDTO> quizzes = quizService.findQuizzes(pageNumber, pageSize);
        int totalPages = quizService.findAll().size(); // FIXME: 더 효율적으로 리팩토링 필요

        for (QuizViewDTO quiz : quizzes) {
            Integer userId = customUserDetails.getUserId();
            Boolean isSolved = submissionService.findById(userId, quiz.getQuizId());
            quiz.setIsCorrect(isSolved);
        }

        model.addAttribute("quizzes", quizzes);

        // paging 객체를 따로 둬서 페이지관리자 따로 두기
        Paging paging = new Paging(pageNumber, (int) Math.ceil((double) totalPages / pageSize));
        model.addAttribute("paging", paging);

        return "quizList";
    }

    @GetMapping("/quizzes/{id}")
    public String getQuiz(@PathVariable int id,
                          Model model,
                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // FIXME: Optional 타입 처리
        int answerCounts = quizService.findAnswersById(id).size(); // 여기서 문제 생김!!
        Quiz quiz = quizService.findById(id).get();
        quiz.setAnswerCounts(answerCounts);

        // 퀴즈 풀이 여부 체킹
        QuizViewDTO quizViewDTO = new QuizViewDTO(quiz);
        Integer userId = customUserDetails.getUserId();
        Boolean isSolved = submissionService.findById(userId, id);
        quizViewDTO.setIsCorrect(isSolved);

        model.addAttribute("quiz", quizViewDTO);

        return "quiz";
    }

    @GetMapping("/new-quiz")
    public String newQuiz(@RequestParam(required = false) Integer id, Model model) {

        // userId와 quizId 통해서 풀이 여부 가져오기 boolean
        // model에 추가하기

        if (id == null) {
            model.addAttribute("quiz", Quiz.builder().build());
        } else {
            Quiz quiz = quizService.findById(id).get(); // FIXME: Optional 예외 처리
            model.addAttribute("quiz", quiz);
        }

        return "newQuiz";
    }
}
