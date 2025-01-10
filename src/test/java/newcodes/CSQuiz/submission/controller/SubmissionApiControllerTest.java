package newcodes.CSQuiz.submission.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import newcodes.CSQuiz.auth.domain.User;
import newcodes.CSQuiz.auth.dto.CustomUserDetails;
import newcodes.CSQuiz.exception.validation.SubmissionValidationException;
import newcodes.CSQuiz.quiz.dto.QuizViewDto;
import newcodes.CSQuiz.submission.dto.SubmissionRequest;
import newcodes.CSQuiz.submission.dto.SubmissionResponse;
import newcodes.CSQuiz.submission.dto.UserAnswer;
import newcodes.CSQuiz.submission.service.SubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

class SubmissionApiControllerTest {

    @Mock
    private SubmissionService submissionService;

    @Mock
    private Model model;

    @InjectMocks
    private SubmissionApiController submissionApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유저가 제출한 답안을 성공적으로 채점한다")
    void gradeUserSubmission() {
        // given
        SubmissionRequest submissionRequest = new SubmissionRequest();
        submissionRequest.setQuizId(1);
        submissionRequest.setUserAnswers(List.of(new UserAnswer("정답")));

        CustomUserDetails customUserDetails = new CustomUserDetails(new User());
        customUserDetails.setUserId(1);

        SubmissionResponse submissionResponse = new SubmissionResponse();
        submissionResponse.setIsAllCorrect(true);

        BindingResult bindingResult = mock(BindingResult.class);

        when(submissionService.gradeSubmission(submissionRequest)).thenReturn(submissionResponse);
        when(submissionService.findById(1, 1)).thenReturn(true);

        // when
        String viewName = submissionApiController.gradeUserSubmission(submissionRequest, customUserDetails,
                bindingResult, model);

        // then
        assertEquals("/quiz :: #answerResult", viewName);
        verify(submissionService).gradeSubmission(submissionRequest);
        verify(submissionService).findById(1, 1);
        verify(model).addAttribute(eq("quiz"), any(QuizViewDto.class));
        verify(model).addAttribute(eq("answerResponse"), eq(submissionResponse));
    }

    @Test
    @DisplayName("유저가 잘못된 형식으로 제출한 답안을 검증한다")
    void gradeUserInvalidSubmission2() {
        // given
        SubmissionRequest submissionRequest = new SubmissionRequest();
        submissionRequest.setQuizId(1);
        submissionRequest.setUserAnswers(List.of(new UserAnswer("")));

        CustomUserDetails customUserDetails = new CustomUserDetails(new User());
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("submissionRequest", "userAnswers", "답안을 입력해주세요")
        ));

        // when & then
        assertThrows(SubmissionValidationException.class, () ->
                submissionApiController.gradeUserSubmission(submissionRequest, customUserDetails, bindingResult, model)
        );
    }
}