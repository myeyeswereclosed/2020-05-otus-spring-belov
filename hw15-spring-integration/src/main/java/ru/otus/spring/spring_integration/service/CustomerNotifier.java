package ru.otus.spring.spring_integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_integration.domain.Transaction;

@Service
public class CustomerNotifier {
    private static final Logger logger = LoggerFactory.getLogger(CustomerNotifier.class);

    public void notify(Transaction transaction) {
        var customer = transaction.getPayment().getCustomer();

        logger.info("Notifying customer {} ({})", customer.fullName(), customer.getEmail());

        //some notifying logic

        logger.info("Notification was sent to {}", customer.getEmail());
    }
}
