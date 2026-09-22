package com.quiz.api.resource;

import com.quiz.api.dto.QuestionRequest;
import com.quiz.api.dto.QuizRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class QuizQuestionResourceTest {
    private long quizId() {
        QuizRequest request = new QuizRequest();
        request.title = "Geography";
        return given().contentType(ContentType.JSON).body(request).post("/quiz-sets")
                .then().statusCode(201).extract().jsonPath().getLong("id");
    }

    private QuestionRequest request(long quizId) {
        QuestionRequest request = new QuestionRequest();
        request.question = "Capital of Indonesia?";
        request.options = List.of("Jakarta", "Bandung", "Surabaya");
        request.answer = "Jakarta";
        request.quizSetId = quizId;
        return request;
    }

    private long create(long quizId) {
        return given().contentType(ContentType.JSON).body(request(quizId))
                .post("/quiz-sets/questions").then().statusCode(201)
                .body("options", hasItems("Jakarta", "Bandung", "Surabaya"))
                .extract().jsonPath().getLong("id");
    }

    @Test void createAndRead() {
        long quizId = quizId();
        long id = create(quizId);
        given().get("/quiz-sets/questions/" + id).then().statusCode(200)
                .body("quizSetId", is((int) quizId), "options[0]", is("Jakarta"));
        given().get("/quiz-sets/" + quizId).then().statusCode(200)
                .body("questions.id", hasItem((int) id));
    }

    @Test void validation() {
        long quizId = quizId();
        QuestionRequest request = request(quizId);
        request.question = " ";
        invalid(request);
        request = request(quizId);
        request.options = List.of();
        invalid(request);
        request = request(quizId);
        request.options = List.of("Jakarta");
        invalid(request);
        request = request(quizId);
        request.options = List.of("Jakarta", " ");
        invalid(request);
        request = request(quizId);
        request.options = List.of("Jakarta", "Bandung");
        request.answer = "Surabaya";
        invalid(request);
        request = request(quizId);
        request.quizSetId = 0L;
        invalid(request);
    }

    private void invalid(QuestionRequest request) {
        given().contentType(ContentType.JSON).body(request).post("/quiz-sets/questions")
                .then().statusCode(400).body("code", is("VALIDATION_ERROR"));
    }

    @Test void missingQuiz() {
        given().contentType(ContentType.JSON).body(request(999999))
                .post("/quiz-sets/questions").then().statusCode(404)
                .body("code", is("QUIZ_SET_NOT_FOUND"));
    }

    @Test void optionsMustBeJsonArray() {
        String payload = "{\"question\":\"Capital?\",\"options\":\"[\\\"Jakarta\\\",\\\"Bandung\\\"]\",\"answer\":\"Jakarta\",\"quizSetId\":" + quizId() + "}";
        given().contentType(ContentType.JSON).body(payload)
                .post("/quiz-sets/questions").then().statusCode(400)
                .body("code", is("VALIDATION_ERROR"));
    }

    @Test void update() {
        long quizId = quizId();
        long id = create(quizId);
        QuestionRequest request = request(quizId);
        request.question = "Capital of West Java?";
        request.answer = "Bandung";
        given().contentType(ContentType.JSON).body(request)
                .put("/quiz-sets/questions/" + id).then().statusCode(200)
                .body("answer", is("Bandung"));
        given().get("/quiz-sets/questions/" + id).then().body("answer", is("Bandung"));
        request.quizSetId = 999999L;
        given().contentType(ContentType.JSON).body(request)
                .put("/quiz-sets/questions/" + id).then().statusCode(404)
                .body("code", is("QUIZ_SET_NOT_FOUND"));
    }

    @Test void delete() {
        long id = create(quizId());
        given().delete("/quiz-sets/questions/" + id).then().statusCode(204);
        given().get("/quiz-sets/questions/" + id).then().statusCode(404)
                .body("code", is("QUESTION_NOT_FOUND"));
    }

    @Test void deletingQuizRemovesQuestions() {
        long quizId = quizId();
        long questionId = create(quizId);
        given().delete("/quiz-sets/" + quizId).then().statusCode(204);
        given().get("/quiz-sets/questions/" + questionId).then().statusCode(404);
    }

    @Test void missingQuestion() {
        given().get("/quiz-sets/questions/999999").then().statusCode(404).body("code", is("QUESTION_NOT_FOUND"));
        given().contentType(ContentType.JSON).body(request(quizId()))
                .put("/quiz-sets/questions/999999").then().statusCode(404)
                .body("code", is("QUESTION_NOT_FOUND"));
        given().delete("/quiz-sets/questions/999999").then().statusCode(404)
                .body("code", is("QUESTION_NOT_FOUND"));
    }
}
