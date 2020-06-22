package ru.otus.spring.spring_boot_testing_app.service.informer;

public interface Informer {
    void inform(String message);

    void startMessage();

    void chooseAnswer();

    void errorMessage();
}
