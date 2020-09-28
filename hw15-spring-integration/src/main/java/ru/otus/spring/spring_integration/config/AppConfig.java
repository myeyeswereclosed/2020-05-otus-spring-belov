package ru.otus.spring.spring_integration.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import ru.otus.spring.spring_integration.domain.Callback;
import ru.otus.spring.spring_integration.domain.Payment;
import ru.otus.spring.spring_integration.domain.Transaction;
import ru.otus.spring.spring_integration.service.*;

import java.util.UUID;
import java.util.concurrent.Executors;

@Configuration
@PropertySource("classpath:application.properties")
@Data
public class AppConfig {
    private static final String PAYMENT_CHANNEL = "paymentChannel";
    private static final String REGISTER_CHANNEL = "registerChannel";
    private static final String ROUTING_CHANNEL = "routingChannel";
    private static final String SALE_CHANNEL = "saleChannel";
    private static final String RECURRING_CHANNEL = "recurringChannel";
    private static final String FINALIZATION_CHANNEL = "finalizationChannel";
    private static final String ERROR_CHANNEL = "errorChannel";

    @Value("${app.paymentQueueCapacity}")
    private int paymentQueueCapacity;

    @Value("${app.pollInterval}")
    private int pollInterval;

    @Value("${app.messagesNumberPerPoll}")
    private int messagesNumberPerPoll;

    @Value("${app.paymentFinalizationPoolSize}")
    private int paymentFinalizationPoolSize;

    @Bean
    public MessageChannel paymentChannel() {
        return MessageChannels.queue(paymentQueueCapacity).get();
    }

    @Bean
    public MessageChannel registerChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public MessageChannel routingChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean (name = PollerMetadata.DEFAULT_POLLER )
    public PollerMetadata poller () {
        return
            Pollers
                .fixedRate(pollInterval)
                .maxMessagesPerPoll(messagesNumberPerPoll)
                .get();
    }

    @Bean
    public IntegrationFlow paymentFlow() {
        return
            IntegrationFlows
                .from(PAYMENT_CHANNEL)
                .handle(service(RegisterService.class), "register")
                .channel(REGISTER_CHANNEL)
                .channel(ROUTING_CHANNEL)
                .get();
    }

    @Bean
    public IntegrationFlow saleFlow() {
        return
            IntegrationFlows
                .from(SALE_CHANNEL)
                .handle(service(SaleService.class), "run")
                .channel(FINALIZATION_CHANNEL)
                .get();
    }

    @Bean
    public IntegrationFlow recurringFlow() {
        return
            IntegrationFlows
                .from(RECURRING_CHANNEL)
                .handle(service(RecurringService.class), "run")
                .channel(FINALIZATION_CHANNEL)
                .get();
    }

    @Bean
    public IntegrationFlow finalizationFlow() {
        return
            IntegrationFlows
                .from(FINALIZATION_CHANNEL)
                .publishSubscribeChannel(
                    Executors.newFixedThreadPool(2),
                    publishSubscribeSpec ->
                        publishSubscribeSpec
                            .subscribe(
                                flow ->
                                    flow
                                        .transform(
                                            Transaction.class,
                                            transaction -> new Callback(UUID.randomUUID(), transaction)
                                        )
                                        .handle(service(MerchantCallbackSender.class), "send")
                            )
                            .subscribe(flow -> flow.handle(service(CustomerNotifier.class), "notify"))
                            .subscribe(flow -> flow.handle(service(FeeService.class), "run"))
                )
                .get();
    }

    @Bean
    public IntegrationFlow routerFlow() {
        return
            IntegrationFlows
                .from(ROUTING_CHANNEL)
                .route(
                    Payment.class,
                    payment -> {
                        if (!payment.isError()) {
                            if (payment.isSale()) {
                                return SALE_CHANNEL;
                            }

                            if (payment.isRecurring()) {
                                return RECURRING_CHANNEL;
                            }
                        }

                        return ERROR_CHANNEL;
                    }
                )
                .get();
    }

    private<T> String service(Class<T> serviceClass) {
        var className = serviceClass.getSimpleName();

        var builder = new StringBuilder(className);

        builder.setCharAt(0, Character.toLowerCase(className.charAt(0)));

        return builder.toString();
    }
}
