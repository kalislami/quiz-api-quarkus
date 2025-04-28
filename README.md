# Quiz API with Quarkus

This project is a simple **Quiz API** built using **Quarkus**.  
It provides endpoints to manage **Quiz Sets** and their **Questions**, including CRUD operations and tag-based queries.

## Features
- Create, read, update, delete Quiz Sets
- Create, update, delete Questions inside a Quiz Set
- Filter Quiz Sets by tags
- Built with **Quarkus** for fast and lightweight REST API
- Database persistence with **Panache Entity**

## API Endpoints

| Method | Path | Description |
|:------:|:----:|:-----------|
| `GET` | `/quiz-sets` | Get all quiz sets |
| `GET` | `/quiz-sets/{id}` | Get a quiz set by ID |
| `POST` | `/quiz-sets` | Create a new quiz set |
| `PUT` | `/quiz-sets/{id}` | Update an existing quiz set |
| `DELETE` | `/quiz-sets/{id}` | Delete a quiz set |
| `POST` | `/quiz-sets/questions` | Add a question to a quiz set |
| `PUT` | `/quiz-sets/questions/{id}` | Update a question |
| `DELETE` | `/quiz-sets/questions/{id}` | Delete a question |
| `GET` | `/quiz-sets/by-tag/{tag}` | Find quiz sets by a specific tag |

## Technologies Used
- [Quarkus](https://quarkus.io/) (Java framework)
- Jakarta REST (JAX-RS)
- Hibernate ORM with Panache
- H2 Database (development)
- JUnit 5 (testing)

## Project Structure

```
src/main/java/com/quiz/api/
 ├── dto/          // Request and response DTOs
 ├── model/        // Entity models
 ├── resource/     // REST API resources
```

## Running the Project

1. **Clone this repo**
   ```bash
   git clone https://github.com/your-username/quiz-api.git
   cd quiz-api
   ```

2. **Start in development mode**
   ```bash
   ./mvnw quarkus:dev
   ```

3. **Access API at**
   ```
   http://localhost:8080/quiz-sets
   ```

## Running Tests

```bash
./mvnw test
```

## Tests Report Location

```bash
/target/jacoco-report/index.html
```

## Run with Docker Compose

```bash
./mvnw clean package -DskipTests

docker compose up --build
```

## Future Improvements
- Authentication & Authorization
- Pagination for quiz listing
- Bulk upload of questions
- Swagger / OpenAPI documentation

---

