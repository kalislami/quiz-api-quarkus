package com.quiz.api.resource;

import com.quiz.api.dto.QuizRequest;
import com.quiz.api.dto.QuizResponse;
import com.quiz.api.service.QuizSetService;
import jakarta.inject.Inject;
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
    @Inject QuizSetService service;

    @GET
    public List<QuizResponse> list() {
        return service.list();
    }

    @GET @Path("/{id}")
    public QuizResponse get(@PathParam("id") Long id) {
        return service.get(id);
    }

    @POST
    public Response create(@Valid QuizRequest request) {
        QuizResponse result = service.create(request);
        return Response.created(URI.create("/quiz-sets/" + result.id)).entity(result).build();
    }

    @PUT @Path("/{id}")
    public QuizResponse update(@PathParam("id") Long id, @Valid QuizRequest request) {
        return service.update(id, request);
    }

    @DELETE @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @GET @Path("/by-tag/{tag}")
    public List<QuizResponse> byTag(@PathParam("tag") String tag) {
        return service.byTag(tag);
    }
}
