package newcodes.CSQuiz.service;

import lombok.RequiredArgsConstructor;
import newcodes.CSQuiz.repository.SubmissionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public Boolean findById(int userId, int quizId) {
        return submissionRepository.findById(userId, quizId);
    }
}
