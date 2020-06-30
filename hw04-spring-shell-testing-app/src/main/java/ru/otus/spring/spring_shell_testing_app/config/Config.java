package ru.otus.spring.spring_shell_testing_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "test")
public class Config {
    private String questionsDelimiter;
    private int questionItemsMinNumber;
    private Locale locale;
    private int pointsToPass;
    private String answersDelimiter;

    public String getQuestionsDelimiter() {
        return questionsDelimiter;
    }

    public void setQuestionsDelimiter(String questionsDelimiter) {
        this.questionsDelimiter = questionsDelimiter;
    }

    public int getQuestionItemsMinNumber() {
        return questionItemsMinNumber;
    }

    public void setQuestionItemsMinNumber(int questionItemsMinNumber) {
        this.questionItemsMinNumber = questionItemsMinNumber;
    }

    public String getAnswersDelimiter() {
        return answersDelimiter;
    }

    public void setAnswersDelimiter(String answersDelimiter) {
        this.answersDelimiter = answersDelimiter;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getPointsToPass() {
        return pointsToPass;
    }

    public void setPointsToPass(int pointsToPass) {
        this.pointsToPass = pointsToPass;
    }
}
