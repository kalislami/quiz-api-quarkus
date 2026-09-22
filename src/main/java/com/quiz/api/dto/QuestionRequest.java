package com.quiz.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class QuestionRequest {

    @NotBlank(message = "Pertanyaan tidak boleh kosong")
    @Size(max = 255)
    public String question;

    @NotEmpty(message = "Options are required")
    @Size(min = 2, message = "At least two options are required")
    public List<@NotBlank(message = "Option must not be blank") @Size(max = 255) String> options;

    @NotBlank(message = "Jawaban tidak boleh kosong")
    @Size(max = 255)
    public String answer;

    @NotNull(message = "QuizSet ID harus ada")
    @Positive(message = "Quiz set ID must be positive")
    public Long quizSetId;
}
