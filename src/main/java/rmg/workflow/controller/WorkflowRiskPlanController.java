package rmg.workflow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rmg.workflow.exceptions.AppIllegalStateException;
import rmg.workflow.exceptions.NoDataFoundException;
import rmg.workflow.model.dto.CompleteDto;
import rmg.workflow.model.dto.ResponseDto;
import rmg.workflow.model.dto.RiskDto;
import rmg.workflow.service.WorkflowRiskPlanService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/risk-plan")
public class WorkflowRiskPlanController {

    private final WorkflowRiskPlanService workflowRiskPlanService;

    @PostMapping("/start-risk-plan-process")
    public ResponseEntity<ResponseDto> startRiskProcess(@RequestBody RiskDto riskDto) throws AppIllegalStateException, NoDataFoundException {

        return ResponseEntity.ok(new ResponseDto(true, null,
                workflowRiskPlanService.startRiskPlanProcess(riskDto),
                HttpStatus.OK.value()));
    }


    @PostMapping("/get-risk-plan-process")
    public ResponseEntity<ResponseDto> getRiskProcess(@RequestBody RiskDto riskDto) throws NoDataFoundException {
        return ResponseEntity.ok(new ResponseDto(true, null,
                workflowRiskPlanService.getRiskPlanProcess(riskDto),
                HttpStatus.OK.value()));
    }


    @PostMapping("/complete-risk-plan-process")
    public ResponseEntity<ResponseDto> completeRiskProcess(@RequestBody CompleteDto completeDto) throws AppIllegalStateException, NoDataFoundException {
        return ResponseEntity.ok(new ResponseDto(true, null,
                workflowRiskPlanService.completeRiskPlanProcess(completeDto),
                HttpStatus.OK.value()));
    }
}
