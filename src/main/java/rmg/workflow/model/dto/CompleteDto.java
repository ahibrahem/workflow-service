package rmg.workflow.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CompleteDto {

    private String action;
    private UUID requestId;
    private String taskId;
    private UUID userId;
    private String notes;
    private UUID newRiskOwnerId;
    private UUID newRiskManagerId;
}
