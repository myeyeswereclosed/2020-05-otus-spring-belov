package ru.otus.spring.spring_integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_integration.domain.Payment;
import ru.otus.spring.spring_integration.domain.Transaction;

import java.util.UUID;

@Service
public class SaleService {
    private static final Logger logger = LoggerFactory.getLogger(SaleService.class);

    @Transformer
    public Transaction run(Payment payment) {
        logger.info("Making transaction from payment");

        var transaction =
            new Transaction(
                UUID.randomUUID(),
                payment.getOrder().getAmount(),
                payment.getOrder().getCurrency(),
                payment
            );

        logger.info("New transaction is " + transaction);

        return transaction;
    }
}
