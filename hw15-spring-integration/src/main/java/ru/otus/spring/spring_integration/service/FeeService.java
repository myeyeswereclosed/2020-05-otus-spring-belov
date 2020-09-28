package ru.otus.spring.spring_integration.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_integration.domain.Transaction;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:fee.properties")
public class FeeService {
    private static final Logger logger = LoggerFactory.getLogger(FeeService.class);

    @Value("${fee.percent}")
    private float feePercent;

    public void run(Transaction transaction) {
        logger.info("Calculating fee for transaction {} ({})", transaction.getId(), transaction.amountInCurrency());

        var value = calculate(transaction);

        logger.info("Fee calculated = {}", value);
    }

    private float calculate(Transaction transaction) {
        return feePercent * transaction.getAmount() / 100;
    }
}
