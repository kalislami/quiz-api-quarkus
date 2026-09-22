package com.quiz.api.service;

import com.quiz.api.dto.QuestionRequest;
import com.quiz.api.dto.QuestionResponse;
import com.quiz.api.exception.ApiException;
import com.quiz.api.mapper.QuestionMapper;
import com.quiz.api.model.QuizQuestion;
import com.quiz.api.model.QuizSet;
import com.quiz.api.repository.QuizQuestionRepository;
import com.quiz.api.repository.QuizSetRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class QuizQuestionService {
    @Inject QuizQuestionRepository repository;
    @Inject QuizSetRepository quizRepository;
    @Inject QuestionMapper mapper;

    public QuestionResponse get(Long id) {
        return mapper.toResponse(find(id));
    }

    @Transactional
    public QuestionResponse create(QuestionRequest request) {
        validateAnswer(request);
        QuizSet quiz = findQuiz(request.quizSetId);
        QuizQuestion question = new QuizQuestion();
        apply(question, request, quiz);
        repository.persist(question);
        return mapper.toResponse(question);
    }

    @Transactional
    public QuestionResponse update(Long id, QuestionRequest request) {
        QuizQuestion question = find(id);
        validateAnswer(request);
        QuizSet quiz = findQuiz(request.quizSetId);
        apply(question, request, quiz);
        return mapper.toResponse(question);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(find(id));
    }

    private QuizQuestion find(Long id) {
        QuizQuestion question = repository.findById(id);
        if (question == null) throw new ApiException(404, "QUESTION_NOT_FOUND", "Question not found");
        return question;
    }

    private QuizSet findQuiz(Long id) {
        QuizSet quiz = quizRepository.findById(id);
        if (quiz == null) throw new ApiException(404, "QUIZ_SET_NOT_FOUND", "Quiz set not found");
        return quiz;
    }

    private void validateAnswer(QuestionRequest request) {
        if (!request.options.contains(request.answer)) {
            throw new ApiException(400, "VALIDATION_ERROR", "Answer must be one of the options");
        }
    }

    private void apply(QuizQuestion question, QuestionRequest request, QuizSet quiz) {
        question.question = request.question;
        question.answer = request.answer;
        question.options.clear();
        question.options.addAll(request.options);
        question.quizSet = quiz;
    }
}
