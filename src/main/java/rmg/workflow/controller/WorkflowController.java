package rmg.workflow.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmg.workflow.exceptions.AppIllegalStateException;
import rmg.workflow.exceptions.NoDataFoundException;
import rmg.workflow.model.dto.CompleteDto;
import rmg.workflow.model.dto.ResponseDto;
import rmg.workflow.model.dto.RiskDto;
import rmg.workflow.service.WorkflowService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflow")
public class WorkflowController {


    private final WorkflowService workflowService;

    @PostMapping("/start-risk-process")
    public ResponseEntity<ResponseDto> startRiskProcess(@RequestBody RiskDto riskDto) throws AppIllegalStateException {

        return ResponseEntity.ok(new ResponseDto(true, null,
                workflowService.startProcess(riskDto),
                HttpStatus.OK.value()));
    }


    @PostMapping("/get-risk-process")
    public ResponseEntity<ResponseDto> getRiskProcess(@RequestBody RiskDto riskDto) throws NoDataFoundException {
        return ResponseEntity.ok(new ResponseDto(true, null,
                workflowService.getRiskProcess(riskDto),
                HttpStatus.OK.value()));
    }


    @PostMapping("/complete-risk-process")
    public ResponseEntity<ResponseDto> completeRiskProcess(@RequestBody CompleteDto completeDto) throws AppIllegalStateException, NoDataFoundException {
        return ResponseEntity.ok(new ResponseDto(true, null,
                workflowService.completeRiskProcess(completeDto),
                HttpStatus.OK.value()));
    }
}
