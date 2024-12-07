package rmg.workflow.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RequestHistoryDto {

    private Long id;
    private Long requestId;
    private ServiceStepActionsDto stepAction;
    private LocalDateTime actionDate;
    private UsersDto assigneeUser;
    private String notes;
}
