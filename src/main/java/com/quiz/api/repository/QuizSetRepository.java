package com.quiz.api.repository;

import com.quiz.api.model.QuizSet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuizSetRepository implements PanacheRepository<QuizSet> {
    // custom query bisa ditaruh di sini kalau butuh
}
