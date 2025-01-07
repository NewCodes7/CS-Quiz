package newcodes.CSQuiz.submission.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.repository.QuizRepository;
import newcodes.CSQuiz.submission.dto.SubmissionRequest;
import newcodes.CSQuiz.submission.dto.SubmissionResponse;
import newcodes.CSQuiz.submission.dto.UserAnswer;
import newcodes.CSQuiz.submission.repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubmissionServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private SubmissionService submissionService;

    private Map<Answer, List<AlternativeAnswer>> answers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Answer answer = new Answer(1, 1, "정답");
        AlternativeAnswer alternativeAnswer = new AlternativeAnswer(1, 1, "대체 정답");
        answers = Map.of(answer, List.of(alternativeAnswer));
    }

    @Test
    @DisplayName("유저가 제출한 답안을 채점하여 맞다는 걸 확인할 수 있다.")
    void gradeCorrectSubmission() {
        // given
        UserAnswer userAnswer = new UserAnswer("정답");
        SubmissionRequest submissionRequest = SubmissionRequest.builder()
                .quizId(1)
                .userId(1)
                .userAnswers(List.of(userAnswer))
                .build();

        when(quizRepository.findAnswersById(1)).thenReturn(answers);

        // when
        SubmissionResponse response = submissionService.gradeSubmission(submissionRequest);

        // then
        assertEquals(1, response.getUserId());
        assertEquals(1, response.getQuizId());
        assertEquals(Collections.singletonList(true), response.getIsCorrect());
        assertEquals(true, response.getIsAllCorrect());
    }

    @Test
    @DisplayName("유저가 제출한 답안을 채점하여 틀렸다는 걸 확인할 수 있다.")
    void gradeIncorrectSubmission() {
        // given
        UserAnswer userAnswer = new UserAnswer("오답");
        SubmissionRequest submissionRequest = SubmissionRequest.builder()
                .quizId(1)
                .userId(1)
                .userAnswers(List.of(userAnswer))
                .build();

        when(quizRepository.findAnswersById(1)).thenReturn(answers);

        // when
        SubmissionResponse response = submissionService.gradeSubmission(submissionRequest);

        // then
        assertEquals(1, response.getUserId());
        assertEquals(1, response.getQuizId());
        assertEquals(Collections.singletonList(false), response.getIsCorrect());
        assertEquals(false, response.getIsAllCorrect());
    }

    // TODO: 답안이 여러 개일 때 테스트 추가
}