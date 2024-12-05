package rmg.workflow.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskDto {

    private Long riskId;
    private Long riskOwnerId;
    private Long riskManagerId;
    private String riskOwnerCode;
    private String riskManagerCode;
    private String currentRole;
    private String notes;


}
