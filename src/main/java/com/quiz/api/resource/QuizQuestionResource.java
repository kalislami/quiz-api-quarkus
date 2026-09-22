package com.quiz.api.resource;

import com.quiz.api.dto.QuestionRequest;
import com.quiz.api.dto.QuestionResponse;
import com.quiz.api.service.QuizQuestionService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/quiz-sets/questions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuizQuestionResource {
    @Inject QuizQuestionService service;

    @POST
    public Response create(@Valid QuestionRequest request) {
        QuestionResponse result = service.create(request);
        return Response.created(URI.create("/quiz-sets/questions/" + result.id)).entity(result).build();
    }

    @GET @Path("/{id}")
    public QuestionResponse get(@PathParam("id") Long id) {
        return service.get(id);
    }

    @PUT @Path("/{id}")
    public QuestionResponse update(@PathParam("id") Long id, @Valid QuestionRequest request) {
        return service.update(id, request);
    }

    @DELETE @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
