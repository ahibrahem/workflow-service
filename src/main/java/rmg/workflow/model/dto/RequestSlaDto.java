package rmg.workflow.model.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RequestSlaDto {

    private Long id;
    private Long requestId;
    private String taskId;
    private Long assigneeUser;
    private Long actionUser;
    private LocalDateTime assignDate;
    private LocalDateTime actionDate;
}
