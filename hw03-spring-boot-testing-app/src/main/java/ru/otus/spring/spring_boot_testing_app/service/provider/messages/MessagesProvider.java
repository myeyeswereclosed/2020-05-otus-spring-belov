package ru.otus.spring.spring_boot_testing_app.service.provider.messages;

public interface MessagesProvider {
    String provide(String message);

    String provide(String message, String[] data);
}
