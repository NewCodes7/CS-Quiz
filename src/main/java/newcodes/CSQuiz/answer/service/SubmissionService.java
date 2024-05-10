package newcodes.CSQuiz.answer.service;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.answer.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public Boolean findById(int userId, int quizId) {
        return submissionRepository.findById(userId, quizId);
    }
}
