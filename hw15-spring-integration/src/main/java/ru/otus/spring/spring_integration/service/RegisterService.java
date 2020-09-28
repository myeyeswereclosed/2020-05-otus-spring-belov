package ru.otus.spring.spring_integration.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_integration.domain.*;
import ru.otus.spring.spring_integration.domain.order.Order;
import ru.otus.spring.spring_integration.repository.merchant.MerchantRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private final MerchantRepository merchantRepository;

    public Payment register(PaymentRequest request) {
        return
            merchantRepository
                .findById(request.getMerchantId())
                .map(merchant ->
                    Optional
                        .ofNullable(request.getRecurringId())
                        .map(id -> makePayment(PaymentType.RECURRING, merchant, request))
                        .orElse(makePayment(PaymentType.SALE, merchant, request))
                )
                .orElseGet(() -> {
                    logger.error("Merchant with id {} not found", request.getMerchantId());

                    return Payment.error();
                })
            ;
    }

    private Payment makePayment(PaymentType paymentType, Merchant merchant, PaymentRequest request) {
        return
            new Payment(
                new Order(
                    UUID.randomUUID(),
                    request.getExternalId(),
                    request.getAmount(),
                    request.getCurrency(),
                    request.getItem()
                ),
                merchant,
                new Customer(
                    request.getCustomerFirstName(),
                    request.getCustomerLastName(),
                    request.getCustomerEmail()
                ),
                paymentType,
                request.getRecurringId()
            );
    }
}
