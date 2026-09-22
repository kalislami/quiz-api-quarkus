package com.quiz.api.repository;

import com.quiz.api.model.QuizSet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class QuizSetRepository implements PanacheRepository<QuizSet> {
    public List<QuizSet> findByTag(String tag) {
        return find("select q from QuizSet q where :tag member of q.tags", java.util.Map.of("tag", tag)).list();
    }
}
