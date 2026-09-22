package com.quiz.api.mapper;

import com.quiz.api.dto.QuestionResponse;
import com.quiz.api.model.QuizQuestion;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class QuestionMapper {
    public QuestionResponse toResponse(QuizQuestion question) {
        return new QuestionResponse(question.id, question.question, List.copyOf(question.options),
                question.answer, question.quizSet.id);
    }
}
