package com.quiz.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class QuizRequest {
   @NotBlank(message = "Judul tidak boleh kosong")
   public String title;

   public String description;
   
   public List<String> tags;
}