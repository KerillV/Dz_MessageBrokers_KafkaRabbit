package netology.creditapplicationservice.controller;

import lombok.RequiredArgsConstructor;
import netology.creditapplicationservice.dto.CreditApplicationParameters;
import netology.creditapplicationservice.dto.CreditApplicationRequest;
import netology.creditapplicationservice.dto.CreditApplicationResponse;
import netology.creditapplicationservice.model.ApplicationStatus;
import netology.creditapplicationservice.service.CreditApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/credit-applications", produces = {"application/json"})
@RequiredArgsConstructor
public class CreditApplicationController {

    private final CreditApplicationService service;

    // создаем заявку на кредит
    @PostMapping
    public CreditApplicationResponse createApplication(@RequestBody CreditApplicationRequest request) {
        Long id = service.createApplication(request);
        return new CreditApplicationResponse(id);
    }

    // получаем ответ статус по идентификатору
    @GetMapping("/{id}/status")
    public ApplicationStatus getApplicationStatus(@PathVariable Long id) {
        return service.getApplicationStatus(id);
    }

    @GetMapping
    public List<CreditApplicationParameters> getAllApplication() {
        return service.getAllApplications();
    }
}