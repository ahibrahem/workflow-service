package rmg.workflow.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class RequestDetailsDto {

    private UUID requestId;
    private UUID riskId;
    private String requestNo;
    private List<RequestHistoryDto> requestHistoryList;
    private ServiceStepsDetailsDto serviceStep;
}
