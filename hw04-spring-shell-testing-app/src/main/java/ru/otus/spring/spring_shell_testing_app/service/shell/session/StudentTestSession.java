package ru.otus.spring.spring_shell_testing_app.service.shell.session;

import org.springframework.stereotype.Component;
import ru.otus.spring.spring_shell_testing_app.domain.StudentTest;
import ru.otus.spring.spring_shell_testing_app.domain.Test;
import ru.otus.spring.spring_shell_testing_app.domain.question.AnsweredQuestion;
import ru.otus.spring.spring_shell_testing_app.domain.question.Question;
import ru.otus.spring.spring_shell_testing_app.domain.student.Student;
import ru.otus.spring.spring_shell_testing_app.infrastructure.AppLogger;
import ru.otus.spring.spring_shell_testing_app.infrastructure.AppLoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class StudentTestSession {
    private static final AppLogger logger = AppLoggerFactory.logger(StudentTestSession.class);

    private Student student;
    private Test test;
    private List<AnsweredQuestion> answeredQuestions = new ArrayList<>();
    private int currentQuestionIndex;

    public StudentTestSession start(Student student) {
        this.student = student;

        return this;
    }

    public Student getStudent() {
        return student;
    }

    public boolean isStarted() {
        return Objects.nonNull(student);
    }

    public void startTest(Test test) {
        this.test = test;
    }

    public boolean isTestInProgress() {
        return Objects.nonNull(test);
    }

    public Optional<Question> currentQuestion() {
        try {
            return Optional.of(test.getQuestions().get(currentQuestionIndex));
        } catch (IndexOutOfBoundsException e) {
            logger.logException(e);
        }

        return Optional.empty();
    }

    public StudentTestSession addAnsweredQuestion(AnsweredQuestion answeredQuestion) {
        answeredQuestions.add(answeredQuestion);

        currentQuestionIndex++;

        return this;
    }

    public Optional<StudentTest> studentTest() {
        return isFinished() ? Optional.of(new StudentTest(student, test, answeredQuestions)) : Optional.empty();
    }

    private boolean isFinished() {
        return currentQuestionIndex == test.getQuestions().size();
    }
}
