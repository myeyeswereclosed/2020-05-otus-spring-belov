package ru.otus.spring.spring_boot_testing_app.domain;

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
