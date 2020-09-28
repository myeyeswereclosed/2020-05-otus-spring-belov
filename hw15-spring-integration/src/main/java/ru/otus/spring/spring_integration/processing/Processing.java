package ru.otus.spring.spring_integration.processing;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_integration.domain.Currency;
import ru.otus.spring.spring_integration.domain.Merchant;
import ru.otus.spring.spring_integration.domain.PaymentRequest;
import ru.otus.spring.spring_integration.domain.Recurring;
import ru.otus.spring.spring_integration.domain.order.Item;
import ru.otus.spring.spring_integration.repository.merchant.MerchantRepository;
import ru.otus.spring.spring_integration.repository.recurring.RecurringRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:processing.properties")
public class Processing {
    private final static Logger logger = LoggerFactory.getLogger(Processing.class);

    private final PaymentSystem paymentSystem;
    private final MerchantRepository merchantRepository;
    private final RecurringRepository recurringRepository;

    @Value("${request.incomeInterval}")
    private int requestIncomeInterval;

    public void run() throws InterruptedException {
        logger.info("Starting payment processing");

        var allMerchants = merchantRepository.findAll();
        var recurrings = recurringRepository.findAll();

        while (true) {
            Thread.sleep(requestIncomeInterval);

            var request = makePaymentRequest(allMerchants, recurrings);

            logger.info("Incoming request: {}", request);

            var payment = paymentSystem.process(request);

            logger.info("Payment registered: {}", payment);
        }
    }

    private PaymentRequest makePaymentRequest(List<Merchant> merchants, List<Recurring> recurrings) {
        var randomKey = RandomUtils.nextInt(0, 10);

        logger.info("Random key = {}", randomKey);

        var isRecurringCall = randomKey % 2 == 0;

        var externalId = "Ext_" + UUID.randomUUID().toString();
        var merchantId = merchants.get(RandomUtils.nextInt(0, merchants.size())).getId();

        var recurringId =
            isRecurringCall
                ? recurrings.get(RandomUtils.nextInt(0, recurrings.size())).getId()
                : UUID.randomUUID();

        var firstName = "First_" + RandomUtils.nextInt(1, 10);
        var lastName = "Last_" + RandomUtils.nextInt(1, 10);

        var currencies = Currency.values();
        var items = Item.values();

        return
            new PaymentRequest(
                externalId,
                items[RandomUtils.nextInt(0, items.length)],
                isRecurringCall ? 0 : RandomUtils.nextInt(100, 1000),
                isRecurringCall ? null : currencies[RandomUtils.nextInt(0, currencies.length)].name(),
                merchantId,
                firstName,
                lastName,
                (firstName + lastName) + "@gmail.com",
                recurringId
            );
    }
}
