package newcodes.CSQuiz.controller;

import java.util.List;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuizViewController {
    private final QuizService quizService;

    @Autowired
    public QuizViewController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/quizzes")
    public String getQuizzes(Model model) {
        List<Quiz> quizzes = quizService.findAll();
        model.addAttribute("quizzes", quizzes);

        return "quizList";
    }

    @GetMapping("/quizzes/{id}")
    public String getQuiz(@PathVariable int id, Model model) {
        // FIXME: Optional 타입 처리
        int answerCounts = quizService.findAnswersById(id).size(); // 여기서 문제 생김!!
        Quiz quiz = quizService.findById(id).get();

        quiz.setAnswerCounts(answerCounts);
        model.addAttribute("quiz", quiz);

        return "quiz";
    }

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
