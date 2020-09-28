package ru.otus.spring.spring_integration.processing;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.spring_integration.domain.Payment;
import ru.otus.spring.spring_integration.domain.PaymentRequest;

@MessagingGateway
public interface PaymentSystem {
    @Gateway(requestChannel = "paymentChannel", replyChannel = "registerChannel")
    Payment process(PaymentRequest incoming);
}
