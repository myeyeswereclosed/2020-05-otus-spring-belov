package ru.otus.spring.students_testing.domain;

import ru.otus.spring.students_testing.domain.question.AnsweredQuestion;
import ru.otus.spring.students_testing.domain.student.Student;

import java.util.List;

public class StudentTest {
    private final Student student;
    private final Test test;
    private final List<AnsweredQuestion> answeredQuestions;

    public StudentTest(Student student, Test test, List<AnsweredQuestion> answeredQuestions) {
        this.student = student;
        this.test = test;
        this.answeredQuestions = answeredQuestions;
    }

    public Student getStudent() {
        return student;
    }

    public List<AnsweredQuestion> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public Test getTest() {
        return test;
    }
}
