package newcodes.CSQuiz.quiz.service;

import java.util.List;
import newcodes.CSQuiz.quiz.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PagingService {
    public <T> Page<T> getPage(List<T> items, int pageNumber, int pageSize) {
        int totalElements = items.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalElements / pageSize));
        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, totalElements);

        return new Page<>(
                items.subList(start, end),
                pageNumber,
                totalPages,
                totalElements
        );
    }
}