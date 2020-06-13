package ru.otus.spring.students_testing.domain;

abstract public class TestResult {
    private final StudentTest testMade;
    private final int points;

    public TestResult(StudentTest studentTest, int points) {
        this.testMade = studentTest;
        this.points = points;
    }

    abstract public boolean isPassed();

    abstract public String status();

    public StudentTest getTestMade() {
        return testMade;
    }

    public int getPoints() {
        return points;
    }
}
