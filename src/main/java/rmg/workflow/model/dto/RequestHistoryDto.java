package rmg.workflow.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RequestHistoryDto {

    private UUID id;
    private UUID requestId;
    private ServiceStepActionsDto stepAction;
    private LocalDateTime actionDate;
    private UsersDto assigneeUser;
    private String notes;
}
