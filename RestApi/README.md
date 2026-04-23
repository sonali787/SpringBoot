# 📦 Spring Boot REST API — Student Management

A basic REST API built with **Spring Boot 4** that performs full CRUD operations on a `Student` resource using an in-memory data store.

---

## 🛠️ Tech Stack

| Technology     | Version  |
|----------------|----------|
| Java           | 21       |
| Spring Boot    | 4.0.6    |
| Spring Web MVC | Included |
| Lombok         | Latest   |
| Maven          | Wrapper  |

---

## 📁 Project Structure

```
RestApi/
├── src/
│   └── main/
│       ├── java/com/example/RestApi/
│       │   ├── RestApiApplication.java         # Main entry point
│       │   ├── controller/
│       │   │   └── StudentController.java      # REST endpoints
│       │   ├── service/
│       │   │   └── StudentService.java         # Business logic
│       │   ├── repository/
│       │   │   └── StudentRepository.java      # In-memory data store
│       │   └── model/
│       │       └── Student.java                # Student POJO
│       └── resources/
│           └── application.properties
└── pom.xml
```

---

## 🚀 How to Run

### Prerequisites
- Java 21+
- Maven (or use the included `mvnw` wrapper)

### Steps

```bash
# Clone the repository
git clone https://github.com/sonali787/SpringBoot.git
cd SpringBoot/RestApi

# Run the application
./mvnw spring-boot:run
```

The server starts on **`http://localhost:8080`**

---

## 🌐 API Endpoints

Base URL: `http://localhost:8080/api/students`

| Method   | Endpoint              | Description              | Status Code |
|----------|-----------------------|--------------------------|-------------|
| `GET`    | `/api/students`       | Get all students         | `200 OK`    |
| `GET`    | `/api/students/{id}`  | Get student by ID        | `200 OK` / `404` |
| `POST`   | `/api/students`       | Create a new student     | `201 Created` |
| `PUT`    | `/api/students/{id}`  | Update existing student  | `200 OK` / `404` |
| `DELETE` | `/api/students/{id}`  | Delete a student         | `200 OK` / `404` |

---

## 📬 Postman Examples

### ✅ GET All Students
```
GET http://localhost:8080/api/students
```

**Response:**
```json
[
  { "id": 1, "name": "Alice Johnson", "email": "alice@example.com", "course": "Computer Science" },
  { "id": 2, "name": "Bob Smith",     "email": "bob@example.com",   "course": "Mathematics" },
  { "id": 3, "name": "Carol Davis",   "email": "carol@example.com", "course": "Physics" }
]
```

---

### ✅ GET Student by ID
```
GET http://localhost:8080/api/students/1
```

**Response:**
```json
{ "id": 1, "name": "Alice Johnson", "email": "alice@example.com", "course": "Computer Science" }
```

---

### ✅ POST — Create Student
```
POST http://localhost:8080/api/students
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Diana Lee",
  "email": "diana@example.com",
  "course": "Chemistry"
}
```

**Response (`201 Created`):**
```json
{ "id": 4, "name": "Diana Lee", "email": "diana@example.com", "course": "Chemistry" }
```

---

### ✅ PUT — Update Student
```
PUT http://localhost:8080/api/students/1
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Alice Updated",
  "email": "alice.new@example.com",
  "course": "Data Science"
}
```

**Response (`200 OK`):**
```json
{ "id": 1, "name": "Alice Updated", "email": "alice.new@example.com", "course": "Data Science" }
```

---

### ✅ DELETE — Delete Student
```
DELETE http://localhost:8080/api/students/2
```

**Response (`200 OK`):**
```
Student with ID 2 deleted successfully.
```

---

## 📌 Notes

- Data is stored **in-memory** — it resets on every restart.
- No database required — perfect for learning REST API basics.
- To connect a real database (e.g., PostgreSQL), add `spring-boot-starter-data-jpa` and configure `application.properties`.

---

## 👩‍💻 Author

**Sonali Yadav** — [github.com/sonali787](https://github.com/sonali787)
