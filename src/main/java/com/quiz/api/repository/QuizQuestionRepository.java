package com.quiz.api.repository;

import com.quiz.api.model.QuizQuestion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuizQuestionRepository implements PanacheRepository<QuizQuestion> {
}
