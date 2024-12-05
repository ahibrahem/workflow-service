package rmg.workflow.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceStepActionsDto {

    private Long id;
    private String actionNameAr;
    private String actionNameEn;
    private String actionCode;
}
