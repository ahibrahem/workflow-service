package rmg.workflow.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ServiceStepActionsDto {

    private UUID id;
    private String actionNameAr;
    private String actionNameEn;
    private String actionCode;
}
