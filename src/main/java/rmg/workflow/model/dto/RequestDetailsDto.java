package rmg.workflow.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class RequestDetailsDto {

    private Long id;
    private Long riskId;
    private String requestNo;
    private List<RequestHistoryDto> requestHistoryList;
    private ServiceStepsDetailsDto serviceStep;
}
