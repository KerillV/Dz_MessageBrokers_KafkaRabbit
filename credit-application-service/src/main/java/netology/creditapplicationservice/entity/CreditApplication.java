package netology.creditapplicationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import netology.creditapplicationservice.model.ApplicationStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private Integer term;
    private BigDecimal income;
    private BigDecimal currentCreditLoad;
    private Integer creditRating;

    // статус заявки (одобрена, отклонена или в процессе)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PROCESSING;

}
