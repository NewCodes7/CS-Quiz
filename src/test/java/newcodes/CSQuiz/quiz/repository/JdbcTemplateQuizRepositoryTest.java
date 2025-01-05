package newcodes.CSQuiz.quiz.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import newcodes.CSQuiz.common.Category;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDto;
import newcodes.CSQuiz.quiz.dto.create.QuizCreateDto;
import newcodes.CSQuiz.quiz.dto.create.QuizDto;
import newcodes.CSQuiz.quiz.dto.create.correctAndAlternativeAnswerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class JdbcTemplateQuizRepositoryTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateQuizRepository repository;

    @BeforeEach
    void setUp() {
        repository = new JdbcTemplateQuizRepository(dataSource);
    }

    @Test
    @DisplayName("퀴즈와 정답을 저장하고 ID로 조회할 수 있다")
    void saveQuizAndAnswersQuiz() {
        // given
        String questionText = "[test]자바의 특징은?";
        QuizCreateDto quizCreateDto = QuizCreateDto.builder()
                .quiz(QuizDto.builder()
                        .categoryId(Category.정보처리기사.getId()) // REFACTOR: 카테고리 영어로..
                        .questionText(questionText)
                        .difficulty("브론즈")
                        .referenceUrl("http://example.com")
                        .blankSentence("자바는 {} 언어입니다.")
                        .build())
                .correctAndAlternativeAnswers(Arrays.asList(correctAndAlternativeAnswerDto.builder()
                        .correctAnswer("객체지향")
                        .alternativeAnswers(Arrays.asList("OOP"))
                        .build()))
                .build();

        // when
        Quiz savedQuiz = repository.saveQuizAndAnswers(quizCreateDto);
        Optional<Quiz> foundQuiz = repository.findQuizById(savedQuiz.getQuizId());

        // then
        assertThat(savedQuiz.getQuizId()).isGreaterThan(0);
        assertThat(foundQuiz).isPresent();
        assertThat(foundQuiz.get().getQuestionText()).isEqualTo(questionText);
        assertThat(foundQuiz.get().getCategoryId()).isEqualTo(Category.정보처리기사.getId());
    }

    @Test
    @DisplayName("검색 조건에 맞는 퀴즈 목록을 조회할 수 있다")
    void findQuizzes() {
        // given
        Quiz quiz = createAndSaveQuizAndAnswersQuiz();
        int userId = 1;
        String keyword = "[test]자바";
        List<String> categories = Arrays.asList("정보처리기사");
        List<String> statuses = Arrays.asList("unsolved");

        // when
        List<QuizViewDto> quizzes = repository.findQuizzes(userId, keyword, categories, statuses);

        // then
        assertThat(quizzes).isNotEmpty();
        assertThat(quizzes).anyMatch(q -> q.getQuestionText().contains("자바"));
    }

    @Test
    @DisplayName("퀴즈 ID로 퀴즈 데이터를 삭제할 수 있다")
    void deleteQuizQuizById() {
        // given
        Quiz quiz = createAndSaveQuizAndAnswersQuiz();

        // when
        repository.deleteQuizById(quiz.getQuizId());

        // then
        Optional<Quiz> deletedQuiz = repository.findQuizById(quiz.getQuizId());
        assertThat(deletedQuiz).isEmpty();
    }

    private Quiz createAndSaveQuizAndAnswersQuiz() {
        QuizCreateDto quizCreateDto = QuizCreateDto.builder()
                .quiz(QuizDto.builder()
                        .categoryId(Category.정보처리기사.getId()) // REFACTOR: 카테고리 영어로..
                        .questionText("[test]자바의 특징은?")
                        .difficulty("브론즈")
                        .referenceUrl("http://example.com")
                        .blankSentence("자바는 {} 언어입니다.")
                        .build())
                .correctAndAlternativeAnswers(Arrays.asList(correctAndAlternativeAnswerDto.builder()
                        .correctAnswer("객체지향")
                        .alternativeAnswers(Arrays.asList("OOP"))
                        .build()))
                .build();

        return repository.saveQuizAndAnswers(quizCreateDto);
    }
}