package com.quiz.api.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class QuizQuestion extends PanacheEntity {

    @NotBlank(message = "Pertanyaan tidak boleh kosong")
    @Column(nullable = false)
    public String question;

    @NotBlank(message = "Opsi tidak boleh kosong")
    @Column(nullable = false, length = 2048)
    public String options; // JSON string

    @NotBlank(message = "Jawaban tidak boleh kosong")
    @Column(nullable = false)
    public String answer;

    @ManyToOne
    @JoinColumn(name = "quiz_set_id")
    public QuizSet quizSet;
}
