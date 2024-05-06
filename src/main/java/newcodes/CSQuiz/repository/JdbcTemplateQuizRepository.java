package newcodes.CSQuiz.repository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import newcodes.CSQuiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.domain.Answer;
import newcodes.CSQuiz.domain.Difficulty;
import newcodes.CSQuiz.domain.Quiz;
import newcodes.CSQuiz.dto.AnswerDTO;
import newcodes.CSQuiz.dto.QuizViewDTO;
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
    public Quiz save(Quiz quiz, Map<Answer, List<AlternativeAnswer>> answers) {
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

        for (Answer answer : answers.keySet()) {
            jdbcTemplate.update(connection -> {
                PreparedStatement pstmt = connection.prepareStatement(answerSql, new String[]{"answer_id"});
                pstmt.setInt(1, quiz.getQuizId());
                pstmt.setString(2, answer.getAnswerText());
                return pstmt;
            }, keyHolder);

            int answerId = keyHolder.getKey().intValue();

            // 대안 답 저장
            for (AlternativeAnswer alternativeAnswer : answers.get(answer)) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement pstmt = connection.prepareStatement(alternativeAnswerSql, new String[]{"alternative_id"});
                    pstmt.setInt(1, answerId);
                    pstmt.setString(2, alternativeAnswer.getAlternativeText());
                    return pstmt;
                }, keyHolder);
            }
        }

        return quiz;
    }

    @Override
    public List<QuizViewDTO> findQuizzes(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT * FROM quizzes LIMIT ?, ?";

        return jdbcTemplate.query(sql, new Object[]{offset, pageSize}, quizDtoRowMapper());
    }

    @Override
    public List<Quiz> findAll() {
        String sql = "SELECT * FROM quizzes";

        return jdbcTemplate.query(sql, quizRowMapper());
    }

    @Override
    public Optional<Quiz> findById(int id) {
        String sql = "SELECT * FROM quizzes where quiz_id = ?";
        List<Quiz> result = jdbcTemplate.query(sql, quizRowMapper(), id);

        return result.stream().findAny();
    }

    @Override
    public Map<Answer, List<AlternativeAnswer>> findAnswersById(int quizId) {
        String answerSql = "SELECT * FROM answers where quiz_id = ?";
        List<Answer> result = jdbcTemplate.query(answerSql, answerRowMapper(), quizId);

        String alternativeAnswerSql = "SELECT * FROM alternative_answers where answer_id = ?";
        Map<Answer, List<AlternativeAnswer>> answers = new HashMap<>();
        for (Answer answer : result) {
            int answerId = answer.getAnswerId();
            List<AlternativeAnswer> alternativeAnswers = jdbcTemplate.query(alternativeAnswerSql, alternativeAnswerRowMapper(), answerId);
            answers.put(answer, alternativeAnswers);
        }

        return answers;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM quizzes where quiz_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Quiz update(Quiz quiz, Map<Answer, List<AlternativeAnswer>> answers) {
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

        for (Answer answer : answers.keySet()) {
            jdbcTemplate.update(connection -> {
                PreparedStatement pstmt = connection.prepareStatement(answerSql, new String[]{"answer_id"});
                pstmt.setInt(1, quiz.getQuizId());
                pstmt.setString(2, answer.getAnswerText());
                return pstmt;
            }, keyHolder);

            int answerId = keyHolder.getKey().intValue();

            // 대안 답 저장
            for (AlternativeAnswer alternativeAnswer : answers.get(answer)) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement pstmt = connection.prepareStatement(alternativeAnswerSql, new String[]{"alternative_id"});
                    pstmt.setInt(1, answerId);
                    pstmt.setString(2, alternativeAnswer.getAlternativeText());
                    return pstmt;
                }, keyHolder);
            }
        }

        return quiz;
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

    private RowMapper<QuizViewDTO> quizDtoRowMapper() {
        return (rs, rowNum) -> {
            Quiz quiz = Quiz.builder()
                    .quizId(rs.getInt("quiz_id"))
                    .categoryId(rs.getInt("category_id"))
                    .questionText(rs.getString("question_text"))
                    .difficulty(rs.getString("difficulty"))
                    .referenceUrl(rs.getString("reference_url")) // Null 가능
                    .blankSentence(rs.getString("blank_sentence"))
                    .build();
            return new QuizViewDTO(quiz);
        };
    }

    private RowMapper<Answer> answerRowMapper() {
        return (rs, rowNum) -> {
            Answer answer = Answer.builder()
                    .answerId(rs.getInt("answer_id"))
                    .quizId(rs.getInt("quiz_id"))
                    .answerText(rs.getString("answer_text"))
                    .build();
            return answer;
        };
    }

    private RowMapper<AlternativeAnswer> alternativeAnswerRowMapper() {
        return (rs, rowNum) -> {
            AlternativeAnswer alternativeAnswer = AlternativeAnswer.builder()
                    .alternativeId(rs.getInt("alternative_id"))
                    .alternativeText(rs.getString("alternative_text"))
                    .answerId(rs.getInt("answer_id"))
                    .build();
            return alternativeAnswer;
        };
    }
}
