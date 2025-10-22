package netology.creditapplicationservice.service;

import lombok.RequiredArgsConstructor;
import netology.creditapplicationservice.dto.CreditApplicationParameters;
import netology.creditapplicationservice.dto.CreditApplicationRequest;
import netology.creditapplicationservice.entity.CreditApplication;
import netology.creditapplicationservice.event.CreditApplicationEvent;
import netology.creditapplicationservice.model.ApplicationStatus;
import netology.creditapplicationservice.repository.CreditApplicationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditApplicationService {
    private final CreditApplicationRepository repository;
    public KafkaTemplate<String, CreditApplicationEvent> kafkaTemplate;

    public Long createApplication(CreditApplicationRequest request) {
        CreditApplication application = new CreditApplication(); // создаем сущность
        BeanUtils.copyProperties(request, application);
        application = repository.save(application); // сохраняем сущность в репозиторий

        // создаем событие
        CreditApplicationEvent event = new CreditApplicationEvent(
                application.getId(),
                application.getAmount(),
                application.getTerm(),
                application.getIncome(),
                application.getCurrentCreditLoad(),
                application.getCreditRating(),
                false
        );

        // отправляем событие с помощью метода send
        kafkaTemplate.send("credit-applications", event); // указываем куда и что отправить
        return application.getId();  // возвращается id
    }

    public ApplicationStatus getApplicationStatus(Long id) {
        return repository.findById(id)
                .map(CreditApplication::getStatus)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @RabbitListener(queues = "credit-decisions")
    // приходит событие, кот-е автоматически распарсилось
    public void handleCreditDecisions(CreditApplicationEvent event) {
        // если находим в репозитории такую сущность, то этой сущности обновляем статус
        repository.findById(event.getApplicationId())
                .ifPresent(application -> {
                    application.setStatus(event.isApproved() ?
                            ApplicationStatus.APPROVED : ApplicationStatus.REJECTED);
                    repository.save(application);
                });
    }

    public List<CreditApplicationParameters> getAllApplications() {
        return repository.findAll().stream()
                .map(app -> new CreditApplicationParameters(
                        app.getId(),
                        app.getAmount(),
                        app.getTerm(),
                        app.getIncome(),
                        app.getCurrentCreditLoad(),
                        app.getCreditRating(),
                        app.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
