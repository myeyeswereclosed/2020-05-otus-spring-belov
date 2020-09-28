package ru.otus.spring.spring_integration.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_integration.domain.Payment;
import ru.otus.spring.spring_integration.domain.Recurring;
import ru.otus.spring.spring_integration.domain.Transaction;
import ru.otus.spring.spring_integration.repository.recurring.RecurringRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecurringService {
    private static final Logger logger = LoggerFactory.getLogger(RecurringService.class);

    private final RecurringRepository repository;

    @Transformer
    public Transaction run(Payment payment) {
        return
            repository
                .findById(payment.getRecurringId())
                .map(
                    recurring -> {
                        logger.info("Making transaction from registered recurring {}", recurring.getId());

                        return makeFromRecurring(recurring, payment);
                    }
                )
                .orElseGet(
                    () -> {
                        logger.info("Registering recurring payment with id {}", payment.getRecurringId());

                        var order = payment.getOrder();

                        return
                            repository
                                .register(
                                    new Recurring(payment.getRecurringId(), order.getAmount(), order.getCurrency())
                                )
                                .map(recurring -> {
                                    logger.info("Recurring {} was successfully registered", recurring.getId());

                                    return makeFromRecurring(recurring, payment);
                                })
                                .orElseGet(() -> {
                                    logger.error(
                                        "Recurring registration error occurred. " +
                                        "Common payment transaction will be provided"
                                    );

                                    return
                                        new Transaction(
                                            UUID.randomUUID(),
                                            order.getAmount(),
                                            order.getCurrency(),
                                            payment
                                        );
                                });
                    });
    }

    private Transaction makeFromRecurring(Recurring recurring, Payment payment) {
        return
            new Transaction(
                UUID.randomUUID(),
                recurring.getAmount(),
                recurring.getCurrency(),
                payment
            );
    }
}
