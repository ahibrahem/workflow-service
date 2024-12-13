package rmg.workflow.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rmg.workflow.model.entity.ServiceStepActions;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ServiceStepsDto {

    private UUID id;
    private String stepNameAr;
    private String stepNameEn;
    private UUID serviceId;
    private String stepCode;
    private List<ServiceStepActionsDto> serviceStepActionList;
}
