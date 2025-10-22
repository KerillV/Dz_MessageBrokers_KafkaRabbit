package netology.creditapplicationservice.event;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CreditDecisionEvent {
    private Long applicationId;
    private boolean approved;

//    public Long getApplicationId() { return applicationId; }
//    public boolean isApproved() { return approved; }

}
