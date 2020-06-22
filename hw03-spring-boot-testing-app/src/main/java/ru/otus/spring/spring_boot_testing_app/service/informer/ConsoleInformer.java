package ru.otus.spring.spring_boot_testing_app.service.informer;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_boot_testing_app.service.provider.messages.MessagesProvider;

@Service
public class ConsoleInformer implements Informer {
    private final static String START_MESSAGE = "test.startMessage";
    private final static String CHOOSE_ANSWER_MESSAGE = "test.chooseAnswer";
    private final static String ERROR_MESSAGE = "test.errorMessage";

    private final MessagesProvider messagesProvider;

    ConsoleInformer(MessagesProvider messagesProvider) {
        this.messagesProvider = messagesProvider;
    }

    @Override
    public void inform(String message) {
        System.out.println(message);
    }

    @Override
    public void startMessage() {
        System.out.println(messagesProvider.provide(START_MESSAGE));
    }

    @Override
    public void chooseAnswer() {
        System.out.println(messagesProvider.provide(CHOOSE_ANSWER_MESSAGE));
    }

    @Override
    public void errorMessage() {
        System.out.println(messagesProvider.provide(ERROR_MESSAGE));
    }
}
