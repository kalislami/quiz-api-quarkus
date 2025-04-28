package com.quiz.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class QuizSet extends PanacheEntity {

   @NotBlank(message = "Judul tidak boleh kosong")
   @Column(nullable = false)
   public String title;

   @Size(max = 1024, message = "Deskripsi tidak boleh lebih dari 1024 karakter")
   @Column(length = 1024)
   public String description;

   @JsonIgnore
   @OneToMany(mappedBy = "quizSet", cascade = CascadeType.ALL, orphanRemoval = true)
   public List<QuizQuestion> questions;

   @ElementCollection
   @CollectionTable(name = "QuizTag", joinColumns = @JoinColumn(name = "quiz_set_id"))
   @Column(name = "tag")
   public List<String> tags = new ArrayList<>();

}
