package newcodes.CSQuiz.quiz.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import newcodes.CSQuiz.quiz.domain.PendingQuiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class JdbcTemplatePendingQuizRepositoryTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplatePendingQuizRepository repository;
    private String sampleQuizRequestBody;

    @BeforeEach
    void setUp() {
        repository = new JdbcTemplatePendingQuizRepository(dataSource);
        sampleQuizRequestBody = createSampleQuizRequestBody();
    }

    @Test
    @DisplayName("대기 중인 퀴즈를 저장하고 ID로 조회할 수 있다")
    void saveQuizAndAnswersPendingQuiz() {
        // given
        PendingQuiz quiz = createSamplePendingQuiz("PENDING");

        // when
        long id = repository.savePendingQuiz(quiz);
        PendingQuiz foundQuiz = repository.findPendingQuizById(id);

        // then
        assertThat(normalizeJsonString(foundQuiz.getRequestBody()))
                .isEqualTo(normalizeJsonString(sampleQuizRequestBody));
        assertThat(foundQuiz.getStatus()).isEqualTo("PENDING");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회시 예외가 발생한다")
    void findByNonExistentId() {
        // when & then
        assertThatThrownBy(() -> repository.findPendingQuizById(999L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("상태별로 대기 중인 퀴즈들을 조회할 수 있다")
    void findPendingQuizByStatus() {
        // given
        PendingQuiz pendingQuiz1 = createSamplePendingQuiz("PENDING");
        PendingQuiz pendingQuiz2 = createSamplePendingQuiz("PENDING");
        PendingQuiz approvedQuiz = createSamplePendingQuiz("APPROVED");

        Long id1 = repository.savePendingQuiz(pendingQuiz1);
        Long id2 = repository.savePendingQuiz(pendingQuiz2);
        Long id3 = repository.savePendingQuiz(approvedQuiz);

        // when
        List<PendingQuiz> pendingQuizzes = repository.findPendingQuizByStatus("PENDING");
        List<PendingQuiz> approvedQuizzes = repository.findPendingQuizByStatus("APPROVED");

        // then
        assertThat(pendingQuizzes)
                .filteredOn(quiz -> quiz.getId().equals(id1) || quiz.getId().equals(id2))
                .hasSize(2)
                .allMatch(quiz -> quiz.getStatus().equals("PENDING"));

        assertThat(approvedQuizzes)
                .filteredOn(quiz -> quiz.getId().equals(id3))
                .hasSize(1)
                .allMatch(quiz -> quiz.getStatus().equals("APPROVED"));
    }

    @Test
    @DisplayName("퀴즈의 상태를 업데이트할 수 있다")
    void updatePendingQuizStatus() {
        // given
        PendingQuiz quiz = createSamplePendingQuiz("PENDING");
        long id = repository.savePendingQuiz(quiz);

        // when
        repository.updatePendingQuizStatus(id, "APPROVED");

        // then
        PendingQuiz updatedQuiz = repository.findPendingQuizById(id);
        assertThat(updatedQuiz.getStatus()).isEqualTo("APPROVED");
    }

    private String createSampleQuizRequestBody() {
        return "{\n"
                + "  \"quiz\": {\n"
                + "    \"categoryId\": 1,\n"
                + "    \"questionText\": \"운영체제에서 프로세스가 CPU를 점유하고 있는 상태를 나타내는 용어는 무엇인가요?\",\n"
                + "    \"difficulty\": \"브론즈\",\n"
                + "    \"referenceUrl\": \"https://example.com/os-process-state\",\n"
                + "    \"blankSentence\": \"프로세스가 CPU를 사용하는 상태는 {}입니다.\"\n"
                + "  },\n"
                + "  \"answers\": [\n"
                + "    {\n"
                + "      \"answerText\": \"Running\",\n"
                + "      \"alternativeAnswers\": [\"실행 중\", \"실행 상태\"]\n"
                + "    }\n"
                + "  ]\n"
                + "}";
    }

    private PendingQuiz createSamplePendingQuiz(String status) {
        return PendingQuiz.builder()
                .requestBody(sampleQuizRequestBody)
                .status(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private String normalizeJsonString(String json) {
        return json.replaceAll("\\s+", "");
    }
}