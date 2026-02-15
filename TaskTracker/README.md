# 🧾 Task Tracker CLI (Java)

A simple Command Line Interface (CLI) application built using **core Java** to manage tasks.
This project demonstrates filesystem handling, manual JSON persistence, and command-line argument parsing **without using any external libraries or frameworks**.

---

## 🚀 Features

* Add tasks
* Update tasks
* Delete tasks
* Mark tasks as:
    * `todo`
    * `in-progress`
    * `done`
* List:
    * All tasks
    * Tasks filtered by status
* Data persisted in a local `task.json` file
* Automatically creates storage file if it doesn’t exist
* No external dependencies (uses only Java standard library)

---

## 🛠 Tech Stack

* Java (JDK 17+ recommended)
* Maven (for compilation only)
* Native Java File APIs (`java.nio.file`)
* Manual JSON serialization (no Jackson/Gson)

---

## 📁 Project Structure

```
TaskTracker/
 ├── src/main/java/org/example
 │       ├── Main.java          # CLI entry point
 │       ├── Task.java          # Task model (OOP design)
 │       └── TaskManager.java   # Business logic + File handling
 │
 ├── task.json                  # Auto-created data file
 ├── pom.xml                    # Maven build config
 └── task-cli                   # Bash launcher script
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone / Download the Project

```bash
git clone <your-repo-url>
cd TaskTracker
```

---

### 2️⃣ Compile the Project

```bash
mvn clean compile
```

This compiles Java files into:
`target/classes`

---

### 3️⃣ Make CLI Executable (First Time Only)

```bash
chmod +x task-cli
```

---

### 4️⃣ Run the CLI

Use:
```bash
./task-cli <command>
```

(Optional) Move it globally:
```bash
sudo mv task-cli /usr/local/bin/task-cli
```

Now you can run:
```bash
task-cli <command>
```

---

## 📌 Usage

### ➕ Add Task

```bash
task-cli add "Buy groceries"
```

Output:
```
Task added successfully (ID: 1)
```

---

### ✏️ Update Task

```bash
task-cli update 1 "Buy groceries and cook dinner"
```

---

### ❌ Delete Task

```bash
task-cli delete 1
```

---

### 🔄 Mark Task Status

```bash
task-cli mark-in-progress 2
task-cli mark-done 2
```

---

### 📋 List All Tasks

```bash
task-cli list
```

---

### 🔍 List Tasks by Status

```bash
task-cli list todo
task-cli list in-progress
task-cli list done
```

---

## 📄 Example `task.json`

```json
[
  {
    "id": 1,
    "description": "Buy groceries",
    "status": "todo",
    "createdAt": "2026-02-16T10:15:22",
    "updatedAt": "2026-02-16T10:15:22"
  }
]
```

---

## 🧠 Key Concepts Demonstrated

* CLI argument parsing using `main(String[] args)`
* File creation & persistence using Java NIO
* Manual JSON serialization/deserialization
* Encapsulation with OOP design
* Exception propagation (`throws`) for CLI-style fail-fast behavior
* Stateless execution with file-backed storage

---

## ▶️ Example Workflow

```bash
task-cli add "Study Java"
task-cli add "Workout"
task-cli list
task-cli mark-done 1
task-cli list done
```

---

## 🏁 Conclusion

This project is designed to simulate how real CLI tools work internally while reinforcing core Java fundamentals without framework abstractions.

---

**Happy Coding! 🚀**
