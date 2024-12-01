package rmg.workflow.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto {

    private Long requestId;
    private String requestNo;
    private Long riskId;
    private List<processInfoDto> processInfoList;
}
