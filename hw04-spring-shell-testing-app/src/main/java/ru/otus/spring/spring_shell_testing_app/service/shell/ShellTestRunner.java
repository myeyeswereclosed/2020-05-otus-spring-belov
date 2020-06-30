package ru.otus.spring.spring_shell_testing_app.service.shell;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.domain.student.Student;
import ru.otus.spring.spring_shell_testing_app.service.shell.answer.AnswerService;
import ru.otus.spring.spring_shell_testing_app.service.shell.login.LoginService;
import ru.otus.spring.spring_shell_testing_app.service.shell.question.QuestionService;
import ru.otus.spring.spring_shell_testing_app.service.shell.starter.TestStarter;

@Service
public class ShellTestRunner implements TestRunner {
    private final LoginService loginService;
    private final TestStarter testStarter;
    private final QuestionService questionService;
    private final AnswerService answerService;

    public ShellTestRunner(
        LoginService loginService,
        TestStarter testStarter,
        QuestionService questionService,
        AnswerService answerService
    ) {
        this.loginService = loginService;
        this.testStarter = testStarter;
        this.questionService = questionService;
        this.answerService = answerService;
    }

    public String login(Student student) {
        return loginService.run(student);
    }

    public String startTest() {
        return testStarter.run();
    }

    public String showQuestion() {
        return questionService.run();
    }

    public String handleAnswer(String answer) {
        return answerService.run(answer);
    }
}
