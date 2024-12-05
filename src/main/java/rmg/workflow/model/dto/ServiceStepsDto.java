package rmg.workflow.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rmg.workflow.model.entity.ServiceStepActions;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ServiceStepsDto {

    private Long id;
    private String stepNameAr;
    private String stepNameEn;
    private Long serviceId;
    private String stepCode;
    private List<ServiceStepActionsDto> serviceStepActionList;
}
