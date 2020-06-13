package ru.otus.spring.students_testing.service.printer;

import org.springframework.stereotype.Service;

@Service
public class ConsoleInformer implements Informer {

    @Override
    public void inform(String message) {
        System.out.println(message);
    }
}
