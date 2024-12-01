package rmg.workflow.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompleteDto {

    private String action;
    private Long requestId;
    private String taskId;

}