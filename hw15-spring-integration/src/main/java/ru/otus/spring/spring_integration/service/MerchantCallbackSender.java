package ru.otus.spring.spring_integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_integration.domain.Callback;

@Service
public class MerchantCallbackSender {
    private static final Logger logger = LoggerFactory.getLogger(MerchantCallbackSender.class);

    public void send(Callback callback) {
        var merchant = callback.getTransaction().getPayment().getMerchant();

        logger.info(
            "Sending callback {} to merchant {} ({})...",
            callback.getId(),
            merchant.getName(),
            merchant.getCallbackUrl()
        );

        // Some sending logic
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        logger.info("Callback {} sent", callback.getId());
    }
}
