package com.quiz.api.resource;

import com.quiz.api.dto.QuestionRequest;
import com.quiz.api.model.QuizSet;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.List;

@QuarkusTest
class QuizSetResourceTest {

   @Test
   void testGetAllQuizSets() {
      given()
            .when().get("/quiz-sets")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
   }

   @Test
   void testGetQuizByTag() {
      given()
            .when().get("/quiz-sets/by-tag/tag-sample")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
   }

   @Test
   void testGetQuizByTagUnkwon() {
      given()
            .when().get("/quiz-sets/by-tag/tag-unknown")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
   }

   @Test
   void testCreateQuizSetSuccess() {
      QuizSet quiz = new QuizSet();
      quiz.title = "Sample Quiz";
      quiz.description = "Testing insert";

      given()
            .contentType(ContentType.JSON)
            .body(quiz)
            .when().post("/quiz-sets")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("title", is("Sample Quiz"));
   }

   @Test
   void testCreateQuizSetValidation() {
      QuizSet quiz = new QuizSet();
      quiz.title = ""; // invalid, harusnya ga boleh kosong
      quiz.description = "Desc";

      given()
            .contentType(ContentType.JSON)
            .body(quiz)
            .when().post("/quiz-sets")
            .then()
            .statusCode(400)
            .body(containsString("Judul tidak boleh kosong"));
   }

   @Test
   void testGetQuizSet() {
      QuizSet quiz = new QuizSet();
      quiz.title = "Initial Title";
      quiz.description = "Initial Desc";

      Long id = given()
            .contentType(ContentType.JSON)
            .body(quiz)
            .when().post("/quiz-sets")
            .then()
            .statusCode(201)
            .extract().jsonPath().getLong("id");

      given()
            .when().get("/quiz-sets/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
   }

   @Test
   void testGetQuizSetNotFound() {
      given()
            .when().get("/quiz-sets/1")
            .then()
            .statusCode(404);
   }

   @Test
   void testUpdateQuizSet() {
      QuizSet quiz = new QuizSet();
      quiz.title = "Initial Title";
      quiz.description = "Initial Desc";

      Long id = given()
            .contentType(ContentType.JSON)
            .body(quiz)
            .when().post("/quiz-sets")
            .then()
            .statusCode(201)
            .extract().jsonPath().getLong("id");

      quiz.title = "Updated Title";
      quiz.description = "Updated Desc";

      given()
            .contentType(ContentType.JSON)
            .body(quiz)
            .when().put("/quiz-sets/" + id)
            .then()
            .statusCode(200)
            .body("title", is("Updated Title"));
   }

   void testUpdateQuizSetNotfoundID() {
      QuizSet quiz = new QuizSet();
      quiz.title = "Initial Title";
      quiz.description = "Initial Desc";

      given()
            .contentType(ContentType.JSON)
            .body(quiz)
            .when().put("/quiz-sets/99")
            .then()
            .statusCode(404);
   }

   @Test
   void testDeleteQuizSet() {
      QuizSet quiz = new QuizSet();
      quiz.title = "To Delete";
      quiz.description = "Temporary";

      Long id = given()
            .contentType(ContentType.JSON)
            .body(quiz)
            .when().post("/quiz-sets")
            .then()
            .statusCode(201)
            .extract().jsonPath().getLong("id");

      given()
            .when().delete("/quiz-sets/" + id)
            .then()
            .statusCode(204);

      given()
            .when().delete("/quiz-sets/" + id)
            .then()
            .statusCode(404);
   }

   @Test
   void testUpdateQuestion() {
      QuizSet quizSet = createSampleQuizSet();

      QuestionRequest createRequest = new QuestionRequest();
      createRequest.question = "Apa ibu kota Malaysia?";
      createRequest.options = "[\"Kuala Lumpur\", \"Jakarta\", \"Bangkok\", \"Hanoi\"]";
      createRequest.answer = "Kuala Lumpur";
      createRequest.quizSetId = quizSet.id;

      Long questionId = given()
            .contentType(ContentType.JSON)
            .body(createRequest)
            .when()
            .post("/quiz-sets/questions")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getLong("id");

      QuestionRequest updateRequest = new QuestionRequest();
      updateRequest.question = "Apa ibu kota Thailand?";
      updateRequest.options = "[\"Bangkok\", \"Kuala Lumpur\", \"Jakarta\", \"Manila\"]";
      updateRequest.answer = "Bangkok";
      updateRequest.quizSetId = quizSet.id;

      given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .when()
            .put("/quiz-sets/questions/" + questionId)
            .then()
            .statusCode(200)
            .body("question", is("Apa ibu kota Thailand?"))
            .body("answer", is("Bangkok"));
   }

   @Test
   void testUpdateQuestionNotFoundQuizSetID() {
      QuizSet quizSet = createSampleQuizSet();

      QuestionRequest createRequest = new QuestionRequest();
      createRequest.question = "Apa ibu kota Malaysia?";
      createRequest.options = "[\"Kuala Lumpur\", \"Jakarta\", \"Bangkok\", \"Hanoi\"]";
      createRequest.answer = "Kuala Lumpur";
      createRequest.quizSetId = quizSet.id;

      Long questionId = given()
            .contentType(ContentType.JSON)
            .body(createRequest)
            .when()
            .post("/quiz-sets/questions")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getLong("id");

      QuestionRequest updateRequest = new QuestionRequest();
      updateRequest.question = "Apa ibu kota Thailand?";
      updateRequest.options = "[\"Bangkok\", \"Kuala Lumpur\", \"Jakarta\", \"Manila\"]";
      updateRequest.answer = "Bangkok";
      updateRequest.quizSetId = (long) 99;

      given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .when()
            .put("/quiz-sets/questions/" + questionId)
            .then()
            .statusCode(400);
   }

   @Test
   void testUpdateQuestionNotFoundQuestionID() {
      QuestionRequest updateRequest = new QuestionRequest();
      updateRequest.question = "Apa ibu kota Thailand?";
      updateRequest.options = "[\"Bangkok\", \"Kuala Lumpur\", \"Jakarta\", \"Manila\"]";
      updateRequest.answer = "Bangkok";
      updateRequest.quizSetId = (long) 99;

      given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .when()
            .put("/quiz-sets/questions/99")
            .then()
            .statusCode(404);
   }

   @Test
   void testAddQuestionNotfoundQuizSetId() {
      QuestionRequest createRequest = new QuestionRequest();
      createRequest.question = "Apa ibu kota Malaysia?";
      createRequest.options = "[\"Kuala Lumpur\", \"Jakarta\", \"Bangkok\", \"Hanoi\"]";
      createRequest.answer = "Kuala Lumpur";
      createRequest.quizSetId = (long) 1;

      given()
            .contentType(ContentType.JSON)
            .body(createRequest)
            .when()
            .post("/quiz-sets/questions")
            .then()
            .statusCode(404);
   }

   @Test
   void testDeleteQuestion() {
      QuizSet quizSet = createSampleQuizSet();

      QuestionRequest createRequest = new QuestionRequest();
      createRequest.question = "Apa ibu kota Filipina?";
      createRequest.options = "[\"Manila\", \"Cebu\", \"Davao\", \"Quezon City\"]";
      createRequest.answer = "Manila";
      createRequest.quizSetId = quizSet.id;

      Long questionId = given()
            .contentType(ContentType.JSON)
            .body(createRequest)
            .when()
            .post("/quiz-sets/questions")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getLong("id");

      given()
            .when()
            .delete("/quiz-sets/questions/" + questionId)
            .then()
            .statusCode(204);
   }

   @Test
   void testDeleteQuestionNotFoundID() {
      given()
            .when()
            .delete("/quiz-sets/questions/99")
            .then()
            .statusCode(404);
   }

   // Helper method untuk menyimpan quizSet dengan transaksi aktif
   @Transactional
   QuizSet createSampleQuizSet() {
      QuizSet quizSet = new QuizSet();
      quizSet.title = "Sample Quiz";
      quizSet.description = "Sample Description";
      quizSet.tags = List.of("tag-sample");
      quizSet.persist();
      return quizSet;
   }
}
