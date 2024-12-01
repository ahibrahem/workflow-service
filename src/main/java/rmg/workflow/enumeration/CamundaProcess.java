package rmg.workflow.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CamundaProcess {

    RISK("RISK"),
    RISK_PLAN("RISK_PLAN"),
    ;
    private final String value;
}
