package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private static final String FILE_NAME = "task.json";

    public TaskManager() throws Exception {
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            Files.writeString(path, "[]");
        }
    }

    // ---------- READ TASKS ----------
    private List<Task> readTasks() throws IOException {

        String content = Files.readString(Paths.get(FILE_NAME)).trim();
        List<Task> tasks = new ArrayList<>();

        if (content.isEmpty() || content.equals("[]"))
            return tasks;

        // 🔥 Remove formatting so manual parser still works
        content = content.replace("\n", "")
                .replace("\r", "")
                .replace("  ", "");

        // Remove [ and ]
        content = content.substring(1, content.length() - 1);

        // Split tasks safely
        String[] entries = content.split("\\},\\{");

        for (String entry : entries) {

            entry = entry.replace("{", "").replace("}", "");

            String[] fields = entry.split(",");

            Map<String, String> map = new HashMap<>();

            for (String f : fields) {
                String[] kv = f.split(":", 2);
                map.put(
                        kv[0].replace("\"", "").trim(),
                        kv[1].replace("\"", "").trim()
                );
            }

            Task t = new Task(
                    Integer.parseInt(map.get("id")),
                    map.get("description")
            );

            t.setStatus(map.get("status"));

            tasks.add(t);
        }

        return tasks;
    }

    // ---------- WRITE TASKS ----------
    private void writeTasks(List<Task> tasks) throws IOException {

        StringBuilder json = new StringBuilder();
        json.append("[\n");   // start array

        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);

            json.append("  {\n");  // indent object

            json.append("    \"id\": ").append(t.getId()).append(",\n");
            json.append("    \"description\": \"").append(t.getDesc()).append("\",\n");
            json.append("    \"status\": \"").append(t.getStatus()).append("\",\n");
            json.append("    \"createdAt\": \"").append(t.getCreatedAt()).append("\",\n");
            json.append("    \"updatedAt\": \"").append(t.getUpdatedAt()).append("\"\n");

            json.append("  }");

            // add comma if not last element
            if (i < tasks.size() - 1) {
                json.append(",");
            }

            json.append("\n");  // newline after each task
        }

        json.append("]\n");  // close array

        Files.writeString(Paths.get(FILE_NAME), json.toString());
    }

    public void add(String desc) throws IOException {
        List<Task> tasks = readTasks();
        int id = tasks.size() + 1;
        Task t = new Task(id, desc);
        tasks.add(t);
        writeTasks(tasks);

    }

    public void update(String id, String desc) throws IOException {
        List<Task> tasks = readTasks();

        for (Task task : tasks) {
            if (task.getId() == Integer.parseInt(id)) {
                task.setDesc(desc);
            }
        }
        writeTasks(tasks);
    }

    public void delete(String id) throws IOException {
        List<Task> tasks = readTasks();
        int taskid = Integer.parseInt(id);

        tasks.removeIf(task -> task.getId() == taskid);
        writeTasks(tasks);
        System.out.println("Task deleted");

    }

    public void markInProgress(String id) throws IOException {

        changeStatus(id, "in-progress");
    }

    public void markDone(String id) throws IOException {
        changeStatus(id, "done");
    }

    public void list() throws IOException {
        List<Task> tasks = readTasks();

        for (Task t : tasks) {
            System.out.println(
                    t.getId() + " | " +
                            t.getDesc() + " | " +
                            t.getStatus());
        }

    }

    public void list(String filter) throws IOException {
        List<Task> tasks = readTasks();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task t : tasks) {
            if (filter == null || t.getStatus().equalsIgnoreCase(filter)) {
                System.out.println(
                        t.getId() + " | " +
                                t.getDesc() + " | " +
                                t.getStatus());
            }
        }
    }

    private void changeStatus(String idStr, String status) throws IOException {
        int id = Integer.parseInt(idStr);
        List<Task> tasks = readTasks();

        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setStatus(status);
            }
        }
        writeTasks(tasks);
        System.out.println("Task marked as " + status);
    }

}
