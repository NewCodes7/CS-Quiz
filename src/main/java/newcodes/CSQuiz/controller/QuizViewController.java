package newcodes.CSQuiz.controller;

import java.util.List;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
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
//
//    @GetMapping("/quizzes/{id}")
//    public String getQuiz(@PathVariable Long id, Model model) {
//        Article article = blogService.findById(id);
//        model.addAttribute("article", new ArticleViewResponse(article));
//
//        return "article";
//    }
//
//    @GetMapping("/new-article")
//    public String newQuiz(@RequestParam(required = false) Long id, Model model) {
//        if (id == null) {
//            model.addAttribute("article", new ArticleViewResponse());
//        } else {
//            Article article = blogService.findById(id);
//            model.addAttribute("article", new ArticleViewResponse(article));
//        }
//
//        return "newArticle";
//    }
}
