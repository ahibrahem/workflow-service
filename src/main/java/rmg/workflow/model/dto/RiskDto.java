package rmg.workflow.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RiskDto {

    private UUID riskId;
    private UUID riskOwnerId;
    private UUID riskManagerId;
    private UUID currentUser;
    private UUID planId;
    private String notes;
}
