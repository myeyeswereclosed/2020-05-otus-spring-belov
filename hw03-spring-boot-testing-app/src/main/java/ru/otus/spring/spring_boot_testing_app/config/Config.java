package ru.otus.spring.spring_boot_testing_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "test")
public class Config {
    private int pointsToPass;
    private Locale locale;

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
