package com.example.RestApi.repository;

import com.example.RestApi.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();
    private int idCounter = 1;

    public StudentRepository() {
        // Pre-load some sample data
        students.add(new Student(idCounter++, "Alice Johnson", "alice@example.com", "Computer Science"));
        students.add(new Student(idCounter++, "Bob Smith", "bob@example.com", "Mathematics"));
        students.add(new Student(idCounter++, "Carol Davis", "carol@example.com", "Physics"));
    }

    public List<Student> findAll() {
        return students;
    }

    public Optional<Student> findById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst();
    }

    public Student save(Student student) {
        student.setId(idCounter++);
        students.add(student);
        return student;
    }

    public Optional<Student> update(int id, Student updatedStudent) {
        return findById(id).map(existing -> {
            existing.setName(updatedStudent.getName());
            existing.setEmail(updatedStudent.getEmail());
            existing.setCourse(updatedStudent.getCourse());
            return existing;
        });
    }

    public boolean deleteById(int id) {
        return students.removeIf(s -> s.getId() == id);
    }
}
