package com.example.RestApi.service;

import com.example.RestApi.model.Student;
import com.example.RestApi.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(int id) {
        return studentRepository.findById(id);
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> updateStudent(int id, Student student) {
        return studentRepository.update(id, student);
    }

    public boolean deleteStudent(int id) {
        return studentRepository.deleteById(id);
    }
}
