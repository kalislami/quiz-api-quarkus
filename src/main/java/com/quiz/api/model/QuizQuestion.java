package com.quiz.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "Pertanyaan tidak boleh kosong")
    @Column(nullable = false)
    public String question;

    @ElementCollection
    @CollectionTable(name = "QuizOption", joinColumns = @JoinColumn(name = "question_id"))
    @OrderColumn(name = "option_order")
    @Column(name = "option_text", nullable = false)
    public List<String> options = new ArrayList<>();

    @NotBlank(message = "Jawaban tidak boleh kosong")
    @Column(nullable = false)
    public String answer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_set_id", nullable = false)
    public QuizSet quizSet;
}
