CREATE TABLE QuizSet (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1024)
);

CREATE TABLE QuizQuestion (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL,
    answer VARCHAR(255) NOT NULL,
    quiz_set_id BIGINT NOT NULL,
    CONSTRAINT fk_question_quiz FOREIGN KEY (quiz_set_id) REFERENCES QuizSet(id)
);

CREATE TABLE QuizTag (
    quiz_set_id BIGINT NOT NULL,
    tag VARCHAR(255),
    CONSTRAINT fk_tag_quiz FOREIGN KEY (quiz_set_id) REFERENCES QuizSet(id)
);

CREATE TABLE QuizOption (
    question_id BIGINT NOT NULL,
    option_order INTEGER NOT NULL,
    option_text VARCHAR(255) NOT NULL,
    PRIMARY KEY (question_id, option_order),
    CONSTRAINT fk_option_question FOREIGN KEY (question_id) REFERENCES QuizQuestion(id)
);
