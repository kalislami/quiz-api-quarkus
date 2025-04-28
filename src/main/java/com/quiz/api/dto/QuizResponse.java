package com.quiz.api.dto;

import java.util.List;

public class QuizResponse {
    public Long id;
    public String title;
    public String description;
    public List<String> tags;
    public List<QuestionResponse> questions;

    public QuizResponse(Long id, String title, String description, List<String> tags, List<QuestionResponse> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.tags = tags;
    }
}
