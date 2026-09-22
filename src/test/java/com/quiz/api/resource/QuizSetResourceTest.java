package com.quiz.api.resource;

import com.quiz.api.dto.QuizRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class QuizSetResourceTest {
    private QuizRequest request(String title) {
        QuizRequest request = new QuizRequest();
        request.title = title;
        request.description = "Description";
        request.tags = List.of("portfolio-tag");
        return request;
    }

    private long create() {
        return given().contentType(ContentType.JSON).body(request("Initial"))
                .when().post("/quiz-sets").then().statusCode(201)
                .header("Location", containsString("/quiz-sets/"))
                .body("title", is("Initial")).extract().jsonPath().getLong("id");
    }

    @Test void createAndRead() {
        long id = create();
        given().get("/quiz-sets/" + id).then().statusCode(200)
                .body("title", is("Initial"), "questions", hasSize(0));
    }

    @Test void blankTitle() {
        given().contentType(ContentType.JSON).body(request("  "))
                .post("/quiz-sets").then().statusCode(400)
                .body("code", is("VALIDATION_ERROR"), "errors.field", hasItem("title"));
    }

    @Test void listAndFilter() {
        long id = create();
        given().get("/quiz-sets").then().statusCode(200).body("id", hasItem((int) id));
        given().get("/quiz-sets/by-tag/portfolio-tag").then().statusCode(200)
                .body("id", hasItem((int) id));
        given().get("/quiz-sets/by-tag/missing-tag").then().statusCode(200).body("$", hasSize(0));
    }

    @Test void update() {
        long id = create();
        given().contentType(ContentType.JSON).body(request("Updated"))
                .put("/quiz-sets/" + id).then().statusCode(200).body("title", is("Updated"));
        given().get("/quiz-sets/" + id).then().body("title", is("Updated"));
    }

    @Test void delete() {
        long id = create();
        given().delete("/quiz-sets/" + id).then().statusCode(204);
        given().get("/quiz-sets/" + id).then().statusCode(404)
                .body("code", is("QUIZ_SET_NOT_FOUND"));
    }

    @Test void missingQuiz() {
        given().get("/quiz-sets/999999").then().statusCode(404).body("code", is("QUIZ_SET_NOT_FOUND"));
        given().contentType(ContentType.JSON).body(request("Updated"))
                .put("/quiz-sets/999999").then().statusCode(404).body("code", is("QUIZ_SET_NOT_FOUND"));
        given().delete("/quiz-sets/999999").then().statusCode(404).body("code", is("QUIZ_SET_NOT_FOUND"));
    }
}
