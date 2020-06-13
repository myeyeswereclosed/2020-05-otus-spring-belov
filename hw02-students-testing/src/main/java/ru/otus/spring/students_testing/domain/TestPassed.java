package ru.otus.spring.students_testing.domain;

public class TestPassed extends TestResult {
    public TestPassed(StudentTest studentTest, int points) {
        super(studentTest, points);
    }

    @Override
    public boolean isPassed() {
        return true;
    }

    @Override
    public String status() {
        return "Test passed!";
    }
}
