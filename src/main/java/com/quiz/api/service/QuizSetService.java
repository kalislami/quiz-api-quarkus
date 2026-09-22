package com.quiz.api.service;

import com.quiz.api.dto.QuizRequest;
import com.quiz.api.dto.QuizResponse;
import com.quiz.api.exception.ApiException;
import com.quiz.api.mapper.QuizMapper;
import com.quiz.api.model.QuizSet;
import com.quiz.api.repository.QuizSetRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class QuizSetService {
    @Inject QuizSetRepository repository;
    @Inject QuizMapper mapper;

    public List<QuizResponse> list() {
        return repository.listAll().stream().map(mapper::toResponse).toList();
    }

    public List<QuizResponse> byTag(String tag) {
        return repository.findByTag(tag).stream().map(mapper::toResponse).toList();
    }

    public QuizResponse get(Long id) {
        return mapper.toResponse(find(id));
    }

    @Transactional
    public QuizResponse create(QuizRequest request) {
        QuizSet quiz = new QuizSet();
        apply(quiz, request);
        repository.persist(quiz);
        return mapper.toResponse(quiz);
    }

    @Transactional
    public QuizResponse update(Long id, QuizRequest request) {
        QuizSet quiz = find(id);
        apply(quiz, request);
        return mapper.toResponse(quiz);
    }

    @Transactional
    public void delete(Long id) {
        QuizSet quiz = find(id);
        repository.delete(quiz);
    }

    private QuizSet find(Long id) {
        QuizSet quiz = repository.findById(id);
        if (quiz == null) throw new ApiException(404, "QUIZ_SET_NOT_FOUND", "Quiz set not found");
        return quiz;
    }

    private void apply(QuizSet quiz, QuizRequest request) {
        quiz.title = request.title;
        quiz.description = request.description;
        quiz.tags.clear();
        if (request.tags != null) quiz.tags.addAll(new ArrayList<>(request.tags));
    }
}
