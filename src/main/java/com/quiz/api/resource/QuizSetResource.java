package com.quiz.api.resource;

import com.quiz.api.dto.QuestionRequest;
import com.quiz.api.dto.QuestionResponse;
import com.quiz.api.dto.QuizRequest;
import com.quiz.api.dto.QuizResponse;
import com.quiz.api.model.QuizQuestion;
import com.quiz.api.model.QuizSet;

import io.quarkus.panache.common.Parameters;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/quiz-sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuizSetResource {

   @GET
   public List<QuizResponse> getAllQuizSets() {
      return QuizSet.<QuizSet>listAll().stream()
            .map(quizSet -> new QuizResponse(
                  quizSet.id,
                  quizSet.title,
                  quizSet.description,
                  quizSet.tags,
                  quizSet.questions == null ? List.of()
                        : quizSet.questions.stream()
                              .map(q -> new QuestionResponse(q.id, q.question, q.options, q.answer,
                                    quizSet.id))
                              .toList()))
            .toList();
   }

   @GET
   @Path("/{id}")
   public Response getQuizSet(@PathParam("id") Long id) {
      QuizSet quizSet = QuizSet.findById(id);
      if (quizSet == null) {
         return Response.status(Response.Status.NOT_FOUND).build();
      }

      QuizResponse response = new QuizResponse(
            quizSet.id,
            quizSet.title,
            quizSet.description,
            quizSet.tags,
            quizSet.questions == null ? List.of()
                  : quizSet.questions.stream()
                        .map(q -> new QuestionResponse(q.id, q.question, q.options, q.answer, quizSet.id))
                        .toList());

      return Response.ok(response).build();
   }

   @POST
   @Transactional
   public Response createQuizSet(@Valid QuizRequest request) {
      QuizSet quiz = new QuizSet();
      quiz.title = request.title;
      quiz.description = request.description;
      quiz.tags = request.tags;
      quiz.persist();
      return Response.created(URI.create("/quiz-sets/" + quiz.id)).entity(quiz).build();
   }

   @PUT
   @Path("/{id}")
   @Transactional
   public Response updateQuizSet(@PathParam("id") Long id, @Valid QuizRequest updated) {
      QuizSet quizSet = QuizSet.findById(id);
      if (quizSet == null) {
         return Response.status(Response.Status.NOT_FOUND).build();
      }

      quizSet.title = updated.title;
      quizSet.description = updated.description;
      quizSet.tags = updated.tags;

      return Response.ok(quizSet).build();
   }

   @DELETE
   @Path("/{id}")
   @Transactional
   public Response deleteQuizSet(@PathParam("id") Long id) {
      boolean deleted = QuizSet.deleteById(id);
      if (!deleted) {
         return Response.status(Response.Status.NOT_FOUND).build();
      }
      return Response.noContent().build();
   }

   @POST
   @Path("/questions")
   @Transactional
   public Response addQuestion(@Valid QuestionRequest dto) {
      QuizSet quizSet = QuizSet.findById(dto.quizSetId);
      if (quizSet == null) {
         throw new NotFoundException("QuizSet tidak ditemukan dengan ID: " + dto.quizSetId);
      }

      QuizQuestion question = new QuizQuestion();
      question.question = dto.question;
      question.options = dto.options;
      question.answer = dto.answer;
      question.quizSet = quizSet;
      question.persist();

      QuestionResponse response = new QuestionResponse(
            question.id,
            question.question,
            question.options,
            question.answer,
            quizSet.id);

      return Response.status(Response.Status.CREATED).entity(response).build();
   }

   @GET
   @Path("/questions/{id}")
   public Response getQuestionById(@PathParam("id") Long id) {
      QuizQuestion question = QuizQuestion.findById(id);
      if (question == null) {
         return Response.status(Response.Status.NOT_FOUND).build();
      }

      return Response.ok(new QuestionResponse(
            question.id,
            question.question,
            question.options,
            question.answer,
            question.quizSet.id)).build();
   }

   @PUT
   @Path("/questions/{id}")
   @Transactional
   public Response updateQuestion(@PathParam("id") Long id, @Valid QuestionRequest dto) {
      QuizQuestion question = QuizQuestion.findById(id);
      if (question == null) {
         return Response.status(Response.Status.NOT_FOUND).build();
      }

      QuizSet quizSet = QuizSet.findById(dto.quizSetId);
      if (quizSet == null) {
         return Response.status(Response.Status.BAD_REQUEST).entity("QuizSet tidak ditemukan").build();
      }

      question.question = dto.question;
      question.options = dto.options;
      question.answer = dto.answer;
      question.quizSet = quizSet;

      return Response.ok(new QuestionResponse(
            question.id,
            question.question,
            question.options,
            question.answer,
            quizSet.id)).build();
   }

   @DELETE
   @Path("/questions/{id}")
   @Transactional
   public Response deleteQuestion(@PathParam("id") Long id) {
      boolean deleted = QuizQuestion.deleteById(id);
      if (!deleted) {
         return Response.status(Response.Status.NOT_FOUND).build();
      }
      return Response.noContent().build();
   }

   @GET
   @Path("/by-tag/{tag}")
   public List<QuizResponse> getQuizByTag(@PathParam("tag") String tag) {
      List<QuizSet> filtered = QuizSet
            .find("SELECT q FROM QuizSet q WHERE :tag MEMBER OF q.tags", Parameters.with("tag", tag))
            .list();

      return filtered.stream()
            .map(q -> new QuizResponse(
                  q.id,
                  q.title,
                  q.description,
                  q.tags,
                  q.questions == null ? List.of()
                        : q.questions.stream()
                              .map(qq -> new QuestionResponse(
                                    qq.id, qq.question, qq.options, qq.answer, q.id))
                              .toList()))
            .toList();
   }

}
