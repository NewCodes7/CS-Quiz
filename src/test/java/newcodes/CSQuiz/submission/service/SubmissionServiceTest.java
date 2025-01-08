package newcodes.CSQuiz.submission.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

    private Map<Answer, List<AlternativeAnswer>> singleAnswer;
    private Map<Answer, List<AlternativeAnswer>> multipleAnswers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 단일 답안 설정
        Answer answer = new Answer(1, 1, "정답");
        AlternativeAnswer alternativeAnswer = new AlternativeAnswer(1, 1, "대체 정답");
        singleAnswer = Map.of(answer, List.of(alternativeAnswer));

        // 다중 답안 설정
        Answer answer1 = new Answer(1, 1, "첫번째 정답");
        Answer answer2 = new Answer(2, 1, "두번째 정답");
        AlternativeAnswer alt1 = new AlternativeAnswer(1, 1, "첫번째 대체 정답");
        AlternativeAnswer alt2 = new AlternativeAnswer(2, 1, "두번째 대체 정답");
        multipleAnswers = Map.of(
                answer1, List.of(alt1),
                answer2, List.of(alt2)
        );
    }

    @Test
    @DisplayName("단일 답안: 유저가 제출한 답안이 정답인 경우")
    void gradeCorrectSubmission() {
        // given
        UserAnswer userAnswer = new UserAnswer("정답");
        SubmissionRequest submissionRequest = SubmissionRequest.builder()
                .quizId(1)
                .userId(1)
                .userAnswers(List.of(userAnswer))
                .build();

        SubmissionResponse expectedResponse = SubmissionResponse.builder()
                .userId(1)
                .quizId(1)
                .isCorrect(Collections.singletonList(true))
                .isAllCorrect(true)
                .build();

        when(quizRepository.findAnswersById(1)).thenReturn(singleAnswer);
        when(submissionRepository.save(any(SubmissionResponse.class))).thenReturn(expectedResponse);

        // when
        SubmissionResponse response = submissionService.gradeSubmission(submissionRequest);

        // then
        assertEquals(expectedResponse.getUserId(), response.getUserId());
        assertEquals(expectedResponse.getQuizId(), response.getQuizId());
        assertEquals(expectedResponse.getIsCorrect(), response.getIsCorrect());
        assertEquals(expectedResponse.getIsAllCorrect(), response.getIsAllCorrect());
    }

    @Test
    @DisplayName("단일 답안: 유저가 제출한 답안이 오답인 경우")
    void gradeIncorrectSubmission() {
        // given
        UserAnswer userAnswer = new UserAnswer("오답");
        SubmissionRequest submissionRequest = SubmissionRequest.builder()
                .quizId(1)
                .userId(1)
                .userAnswers(List.of(userAnswer))
                .build();

        SubmissionResponse expectedResponse = SubmissionResponse.builder()
                .userId(1)
                .quizId(1)
                .isCorrect(Collections.singletonList(false))
                .isAllCorrect(false)
                .build();

        when(quizRepository.findAnswersById(1)).thenReturn(singleAnswer);
        when(submissionRepository.save(any(SubmissionResponse.class))).thenReturn(expectedResponse);

        // when
        SubmissionResponse response = submissionService.gradeSubmission(submissionRequest);

        // then
        assertEquals(expectedResponse.getUserId(), response.getUserId());
        assertEquals(expectedResponse.getQuizId(), response.getQuizId());
        assertEquals(expectedResponse.getIsCorrect(), response.getIsCorrect());
        assertEquals(expectedResponse.getIsAllCorrect(), response.getIsAllCorrect());
    }

    @Test
    @DisplayName("복수 답안: 모든 답안이 정답인 경우")
    void gradeAllCorrectMultipleSubmissions() {
        // given
        List<UserAnswer> userAnswers = Arrays.asList(
                new UserAnswer("첫번째 정답"),
                new UserAnswer("두번째 정답")
        );

        SubmissionRequest submissionRequest = SubmissionRequest.builder()
                .quizId(1)
                .userId(1)
                .userAnswers(userAnswers)
                .build();

        SubmissionResponse expectedResponse = SubmissionResponse.builder()
                .userId(1)
                .quizId(1)
                .isCorrect(Arrays.asList(true, true))
                .isAllCorrect(true)
                .build();

        when(quizRepository.findAnswersById(1)).thenReturn(multipleAnswers);
        when(submissionRepository.save(any(SubmissionResponse.class))).thenReturn(expectedResponse);

        // when
        SubmissionResponse response = submissionService.gradeSubmission(submissionRequest);

        // then
        assertEquals(expectedResponse.getUserId(), response.getUserId());
        assertEquals(expectedResponse.getQuizId(), response.getQuizId());
        assertEquals(expectedResponse.getIsCorrect(), response.getIsCorrect());
        assertEquals(expectedResponse.getIsAllCorrect(), response.getIsAllCorrect());
    }

    @Test
    @DisplayName("복수 답안: 일부 답안만 정답인 경우")
    void gradePartiallyCorrectMultipleSubmissions() {
        // given
        List<UserAnswer> userAnswers = Arrays.asList(
                new UserAnswer("첫번째 정답"),
                new UserAnswer("오답")
        );

        SubmissionRequest submissionRequest = SubmissionRequest.builder()
                .quizId(1)
                .userId(1)
                .userAnswers(userAnswers)
                .build();

        SubmissionResponse expectedResponse = SubmissionResponse.builder()
                .userId(1)
                .quizId(1)
                .isCorrect(Arrays.asList(true, false))
                .isAllCorrect(false)
                .build();

        when(quizRepository.findAnswersById(1)).thenReturn(multipleAnswers);
        when(submissionRepository.save(any(SubmissionResponse.class))).thenReturn(expectedResponse);

        // when
        SubmissionResponse response = submissionService.gradeSubmission(submissionRequest);

        // then
        assertEquals(expectedResponse.getUserId(), response.getUserId());
        assertEquals(expectedResponse.getQuizId(), response.getQuizId());
        assertEquals(expectedResponse.getIsCorrect(), response.getIsCorrect());
        assertEquals(expectedResponse.getIsAllCorrect(), response.getIsAllCorrect());
    }
}