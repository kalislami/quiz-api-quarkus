package com.quiz.api.mapper;

import com.quiz.api.dto.QuizResponse;
import com.quiz.api.model.QuizSet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class QuizMapper {
    @Inject QuestionMapper questionMapper;

    public QuizResponse toResponse(QuizSet quiz) {
        return new QuizResponse(quiz.id, quiz.title, quiz.description, List.copyOf(quiz.tags),
                quiz.questions == null ? List.of() : quiz.questions.stream().map(questionMapper::toResponse).toList());
    }
}
