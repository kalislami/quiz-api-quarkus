package com.quiz.api.dto;

public class QuestionResponse {
    public Long id;
    public String question;
    public String options;
    public String answer;
    public Long quizSetId;

    public QuestionResponse(Long id, String question, String options, String answer, Long quizSetId) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.quizSetId = quizSetId;
    }
}
