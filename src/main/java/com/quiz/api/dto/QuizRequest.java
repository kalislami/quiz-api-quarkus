package com.quiz.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class QuizRequest {
   @NotBlank(message = "Judul tidak boleh kosong")
   @Size(max = 255)
   public String title;

   @Size(max = 1024)
   public String description;
   
   public List<@NotBlank String> tags;
}
