package rmg.workflow.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto {

    private Long id;
    private Long riskId;
    private String requestNo;
    private List<processInfoDto> processInfoList;
    private List<RequestHistoryDto> requestHistoryList;
    private ServiceStepsDto serviceStep;
}
