package newcodes.CSQuiz.quiz.repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.global.Category;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDTO;
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

        quiz.setQuizId(keyHolder.getKey().intValue());

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

    /*
    * 옵션: 페이징(LIMIT), 키워드(WHERE), 카테고리(WHERE), (풀이 여부)
    */
    public List<QuizViewDTO> findQuizzes(int userId, String kw, List<String> categories, List<String> statuses) {
        String sql = "SELECT q.*, "
                + "(CASE "
                + "WHEN SUM(CASE WHEN s.correct = 1 THEN 1 ELSE 0 END) > 0 THEN true "
                + "ELSE false "
                + "END) AS solved "
                + "FROM quizzes q "
                + "LEFT JOIN submissions s ON q.quiz_id = s.quiz_id AND s.user_id = ? ";
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (!categories.get(0).equals("none")) {
            sql += "WHERE q.category_id IN (";
            String categorySql = categories.stream()
                    .map(c -> {
                        System.out.println(c);
                        return Integer.toString(Category.valueOf(c).getId());
                    })
                    .collect(Collectors.joining(","));
            sql += categorySql + ") ";
        }

        if (kw != null && kw != "") {
            sql += (sql.contains("WHERE") ? "AND " : "WHERE ") + "q.question_text LIKE ? ";
            params.add("%" + kw + "%");
        }

        sql += "GROUP BY q.quiz_id";

        if (!statuses.get(0).equals("none")) {
            sql += " HAVING ";
            boolean hasSolved = statuses.contains("solved");
            boolean hasUnsolved = statuses.contains("unsolved");
            if (hasSolved && hasUnsolved) {
                sql += "1 = 1";
            } else if (hasSolved) {
                sql += "solved = true";
            } else {
                sql += "solved = false";
            }
        }

        sql += ";";

        return jdbcTemplate.query(sql, params.toArray(), quizDtoRowMapper());
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

        // answer 쿼리
        String answerSql = "SELECT * FROM answers where quiz_id = ?";
        List<Answer> result = jdbcTemplate.query(answerSql, answerRowMapper(), quizId);

        // alternativeAnswer 쿼리
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
        String sql = "DELETE FROM alternative_answers where answer_id = ?";
        jdbcTemplate.update(sql, id);

        sql = "DELETE FROM answers where quiz_id = ?";
        jdbcTemplate.update(sql, id);

        sql = "DELETE FROM quizzes where quiz_id = ?";
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

            QuizViewDTO quizViewDTO = new QuizViewDTO(quiz);
            quizViewDTO.setIsCorrect(rs.getBoolean("solved"));

            return quizViewDTO;
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
