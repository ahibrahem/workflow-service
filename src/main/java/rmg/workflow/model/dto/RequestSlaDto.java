package rmg.workflow.model.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RequestSlaDto {

    private UUID id;
    private UUID requestId;
    private String taskId;
    private UUID assigneeUser;
    private UUID actionUser;
    private LocalDateTime assignDate;
    private LocalDateTime actionDate;
}
