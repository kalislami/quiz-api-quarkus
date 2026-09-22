package com.quiz.api.dto;

import java.util.List;

public class QuestionResponse {
    public Long id;
    public String question;
    public List<String> options;
    public String answer;
    public Long quizSetId;

    public QuestionResponse(Long id, String question, List<String> options, String answer, Long quizSetId) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.quizSetId = quizSetId;
    }
}
