package newcodes.CSQuiz.repository;

import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import newcodes.CSQuiz.domain.Difficulty;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.AnswerDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JdbcTemplateQuizRepository implements QuizRepository {

    private static final int ZERO_INITIALIZER = 0;
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateQuizRepository(DataSource dataSource) { // 데이터 소스의 역할? 생성자 하나면 Autowired 생략 가능
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Quiz save(Quiz quiz, List<AnswerDTO> answerRequest) {
        // quiz 저장
        String quizzesSql = "INSERT INTO quizzes (category_id, question_text, difficulty, reference_url, attempt_count, correct_count, blank_sentence) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(quizzesSql, new String[]{"quiz_id"});
            pstmt.setInt(1, quiz.getCategoryId());
            pstmt.setString(2, quiz.getQuestionText());
            pstmt.setString(3, quiz.getDifficulty());
            pstmt.setString(4, quiz.getReferenceUrl());
            pstmt.setInt(5, ZERO_INITIALIZER);
            pstmt.setInt(6, ZERO_INITIALIZER);
            pstmt.setString(7, quiz.getBlankSentence());

            return pstmt;
        }, keyHolder);

        int quizId = keyHolder.getKey().intValue();
        quiz.setQuizId(quizId);

        // answer 저장
        String answerSql = "INSERT INTO answers (quiz_id, answer_text) VALUES (?, ?)";
        String alternativeAnswerSql = "INSERT INTO alternative_answers (answer_id, alternative_text) VALUES (?, ?)";

        for (AnswerDTO request : answerRequest) {
            jdbcTemplate.update(connection -> {
                PreparedStatement pstmt = connection.prepareStatement(answerSql, new String[]{"answer_id"});
                pstmt.setInt(1, quiz.getQuizId());
                pstmt.setString(2, request.getAnswerText());
                return pstmt;
            }, keyHolder);

            int answerId = keyHolder.getKey().intValue();

            // 대안 답 저장
            for (String alternativeAnswer : request.getAlternativeAnswers()) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement pstmt = connection.prepareStatement(alternativeAnswerSql, new String[]{"alternative_id"});
                    pstmt.setInt(1, answerId);
                    pstmt.setString(2, alternativeAnswer);
                    return pstmt;
                }, keyHolder);
            }
        }

        return quiz;
    }

    @Override
    public List<Quiz> findAll() {
        String sql = "SELECT * FROM quizzes";

        return jdbcTemplate.query(sql, quizRowMapper());
    }

    private RowMapper<Quiz> quizRowMapper() {
        return (rs, rowNum) -> {
            Quiz quiz = Quiz.builder()
                    .quizId(rs.getInt("quiz_id"))
                    .categoryId(rs.getInt("category_id"))
                    .questionText(rs.getString("question_text"))
                    .difficulty(rs.getString("difficulty"))
                    .referenceUrl(rs.getString("reference_url")) // Null 가능
                    .blankSentence(rs.getString("blank_sentence"))
                    .build();
            return quiz;
        };
    }
}
