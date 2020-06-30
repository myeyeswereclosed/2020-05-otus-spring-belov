package ru.otus.spring.spring_shell_testing_app.domain;

public class TestFailed extends TestResult {
    public TestFailed(StudentTest studentTest, int points) {
        super(studentTest, points);
    }

    @Override
    public boolean isPassed() {
        return false;
    }

    @Override
    public String status() {
        return "Test was not passed";
    }
}
