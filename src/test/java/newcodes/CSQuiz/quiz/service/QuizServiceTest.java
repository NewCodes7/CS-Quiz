package newcodes.CSQuiz.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import newcodes.CSQuiz.exception.QuizNotFoundException;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("퀴즈 ID를 통해 단일 퀴즈를 조회할 수 있다")
    void findQuizByIdReturnsQuizWhenQuizIsFound() {
        // given
        Quiz quiz = Quiz.builder().quizId(1).questionText("question").build();
        when(quizRepository.findQuizById(1)).thenReturn(Optional.of(quiz));

        // when
        Quiz result = quizService.findQuizById(1);

        // then
        assertEquals(quiz, result);
    }

    @Test
    @DisplayName("퀴즈 ID로 조회시 퀴즈가 존재하지 않으면 QuizNotFoundException이 발생한다")
    void findQuizByIdThrowsQuizNotFoundExceptionWhenQuizIsNotFound() {
        // given & when
        when(quizRepository.findQuizById(1)).thenReturn(Optional.empty());

        // then
        assertThrows(QuizNotFoundException.class, () -> quizService.findQuizById(1));
    }
}
