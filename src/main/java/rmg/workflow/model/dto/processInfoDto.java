package rmg.workflow.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class processInfoDto {

    private Long Id;
    private String taskId;
    private String taskAssignee;
}
