package newcodes.CSQuiz.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.CustomUserDetails;
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
                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Quiz> quizzesTemp = quizService.findAll();

        // userId로 submission correct 정보 가져오기
        List<Boolean> userSubmissions = new ArrayList<>();

        List<QuizViewDTO> quizzes = new ArrayList<>();
        for (Quiz quiz : quizzesTemp) {
            QuizViewDTO quizViewDTO = new QuizViewDTO(quiz);
            Integer userId = customUserDetails.getUserId();
            Boolean isSolved = submissionService.findById(userId, quiz.getQuizId());
            quizViewDTO.setIsCorrect(isSolved);
            quizzes.add(quizViewDTO);
        }

        model.addAttribute("quizzes", quizzes);

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
