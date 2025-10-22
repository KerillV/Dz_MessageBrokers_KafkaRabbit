package netology.creditprocessingservice.service;

import lombok.RequiredArgsConstructor;
import netology.creditapplicationservice.event.CreditApplicationEvent;
import netology.creditapplicationservice.event.CreditDecisionEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CreditProcesssingService {
    private final RabbitTemplate rabbitTemplate;
//    private final RabbitTemplateConfigurer rabbitTemplateConfigurer;

    @KafkaListener(topics = "credit-applications", groupId = "credit-processing-service")
    public void processApplications(CreditApplicationEvent event) {
        // Расчёт месячного платежа
        BigDecimal monthlyPayment = calculateMonthlyPayment(event.getAmount(), event.getTerm());
        // Вычисление общей суммы ежемесячной кредитной нагрузки
        BigDecimal totalMonthlyPayments = monthlyPayment.add(event.getCurrentCreditLoad());
        // Проверка платежеспособности (50% от общего дохода)
        boolean approved = totalMonthlyPayments.doubleValue() <= event.getIncome().doubleValue() * 0.5;

        /* Создаётся новый объект CreditDecisionEvent, который содержит идентификатор заявки и факт принятого
        решения (approved) — либо утвердили кредит, либо отклонили. */
        CreditDecisionEvent decision = new CreditDecisionEvent(event.getApplicationId(), approved);
        rabbitTemplate.convertAndSend("credit-decisions", decision);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term) {
        return amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
    }

}
