package com.quiz.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionRequest {

    @NotBlank(message = "Pertanyaan tidak boleh kosong")
    public String question;

    @NotNull(message = "Pilihan tidak boleh kosong")
    public String options;

    @NotBlank(message = "Jawaban tidak boleh kosong")
    public String answer;

    @NotNull(message = "QuizSet ID harus ada")
    public Long quizSetId;
}
