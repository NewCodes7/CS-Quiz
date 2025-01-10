package newcodes.CSQuiz.quiz.repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import newcodes.CSQuiz.common.Category;
import newcodes.CSQuiz.quiz.domain.AlternativeAnswer;
import newcodes.CSQuiz.quiz.domain.Answer;
import newcodes.CSQuiz.quiz.domain.Quiz;
import newcodes.CSQuiz.quiz.dto.QuizViewDto;
import newcodes.CSQuiz.quiz.dto.create.QuizCreateDto;
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

    @Transactional
    public Quiz saveQuizAndAnswers(QuizCreateDto quizCreateDto) {
        Quiz quiz = quizCreateDto.getQuiz().toEntity();
        Map<Answer, List<AlternativeAnswer>> correctAndAlternativeAnswers = quizCreateDto.getAnswerMap();

        saveQuiz(quiz);
        saveAnswers(quiz.getQuizId(), correctAndAlternativeAnswers);

        return quiz;
    }

    private void saveQuiz(Quiz quiz) {
        String sql = "INSERT INTO quizzes (category_id, question_text, difficulty, reference_url, attempt_count, correct_count, blank_sentence) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql, new String[]{"quiz_id"});
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
    }

    private void saveAnswers(int quizId, Map<Answer, List<AlternativeAnswer>> correctAndAlternativeAnswers) {
        String answerSql = "INSERT INTO answers (quiz_id, answer_text) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        for (Answer correctAnswer : correctAndAlternativeAnswers.keySet()) {
            // 정답 저장
            jdbcTemplate.update(connection -> {
                PreparedStatement pstmt = connection.prepareStatement(answerSql, new String[]{"answer_id"});
                pstmt.setInt(1, quizId);
                pstmt.setString(2, correctAnswer.getAnswerText());
                return pstmt;
            }, keyHolder);

            // 대체 답안 저장
            int correctAnswerId = keyHolder.getKey().intValue();
            saveAlternativeAnswers(correctAndAlternativeAnswers, correctAnswer, correctAnswerId);
        }
    }

    private void saveAlternativeAnswers(Map<Answer, List<AlternativeAnswer>> correctAndAlternativeAnswers,
                                        Answer correctAnswer,
                                        int correctAnswerId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String alternativeAnswerSql = "INSERT INTO alternative_answers (answer_id, alternative_text) VALUES (?, ?)";

        for (AlternativeAnswer alternativeAnswer : correctAndAlternativeAnswers.get(correctAnswer)) {
            jdbcTemplate.update(connection -> {
                PreparedStatement pstmt = connection.prepareStatement(alternativeAnswerSql,
                        new String[]{"alternative_id"});
                pstmt.setInt(1, correctAnswerId);
                pstmt.setString(2, alternativeAnswer.getAlternativeText());
                return pstmt;
            }, keyHolder);
        }
    }

    /*
     * 옵션: 페이징(LIMIT), 키워드(WHERE), 카테고리(WHERE), (풀이 여부)
     */
    public List<QuizViewDto> findQuizzes(int userId, String kw, List<String> categories, List<String> statuses) {
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

    public Optional<Quiz> findQuizById(int id) {
        String sql = "SELECT * FROM quizzes where quiz_id = ?";
        List<Quiz> result = jdbcTemplate.query(sql, quizRowMapper(), id);

        return result.stream().findAny();
    }

    @Transactional
    public Map<Answer, List<AlternativeAnswer>> findAnswersById(int quizId) {
        // answer 쿼리
        String answerSql = "SELECT * FROM answers where quiz_id = ?";
        List<Answer> result = jdbcTemplate.query(answerSql, answerRowMapper(), quizId);

        // alternativeAnswer 쿼리
        String alternativeAnswerSql = "SELECT * FROM alternative_answers where answer_id = ?";

        Map<Answer, List<AlternativeAnswer>> answers = new HashMap<>();
        for (Answer answer : result) {
            int answerId = answer.getAnswerId();
            List<AlternativeAnswer> alternativeAnswers = jdbcTemplate.query(alternativeAnswerSql,
                    alternativeAnswerRowMapper(), answerId);
            answers.put(answer, alternativeAnswers);
        }

        return answers;
    }

    @Transactional
    public void deleteQuizById(int quizId) {
        // 1. 먼저 답변의 대체 답안들을 삭제
        String sql = "DELETE FROM alternative_answers WHERE answer_id IN " +
                "(SELECT answer_id FROM answers WHERE quiz_id = ?)";
        jdbcTemplate.update(sql, quizId);

        // 2. 퀴즈에 연결된 답변들 삭제
        sql = "DELETE FROM answers WHERE quiz_id = ?";
        jdbcTemplate.update(sql, quizId);

        // 3. 퀴즈에 대한 제출 기록 삭제
        sql = "DELETE FROM submissions WHERE quiz_id = ?";
        jdbcTemplate.update(sql, quizId);

        // 4. 마지막으로 퀴즈 자체를 삭제
        sql = "DELETE FROM quizzes WHERE quiz_id = ?";
        jdbcTemplate.update(sql, quizId);
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

    private RowMapper<QuizViewDto> quizDtoRowMapper() {
        return (rs, rowNum) -> {
            Quiz quiz = Quiz.builder()
                    .quizId(rs.getInt("quiz_id"))
                    .categoryId(rs.getInt("category_id"))
                    .questionText(rs.getString("question_text"))
                    .difficulty(rs.getString("difficulty"))
                    .referenceUrl(rs.getString("reference_url")) // Null 가능
                    .blankSentence(rs.getString("blank_sentence"))
                    .build();

            QuizViewDto quizViewDTO = new QuizViewDto(quiz);
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
