package rmg.workflow.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmg.workflow.enumeration.CamundaSteps;
import rmg.workflow.exceptions.AppIllegalStateException;
import rmg.workflow.exceptions.NoDataFoundException;
import rmg.workflow.mapper.RequestMapper;
import rmg.workflow.model.dto.CompleteDto;
import rmg.workflow.model.dto.RequestDto;
import rmg.workflow.model.dto.RiskDto;
import rmg.workflow.model.entity.ProcessInfo;
import rmg.workflow.model.entity.Requests;
import rmg.workflow.model.entity.ServiceSteps;
import rmg.workflow.repository.ProcessInfoRepository;
import rmg.workflow.repository.RequestsRepository;
import rmg.workflow.repository.ServiceStepsRepository;
import rmg.workflow.util.CamundaUtil;
import rmg.workflow.util.CommonUtil;
import rmg.workflow.util.ConstantString;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final RequestsRepository requestsRepository;
    private final ServiceStepsRepository serviceStepsRepository;
    private final CamundaUtil camundaUtil;
    private final RequestMapper requestMapper;
    private final CommonUtil commonUtil;
    private final ProcessInfoRepository processInfoRepository;


    @Transactional(rollbackFor = Exception.class)
    public String startProcess(RiskDto riskDto) throws AppIllegalStateException {

        validateValidRisk(riskDto.getRiskId());

        Requests request = camundaUtil.initRequestObject(ConstantString.RISK_SERVICE, CamundaSteps.OWNER_RISK_ANALYSIS.getValue(), riskDto.getRiskId());

        commonUtil.preparedRequestData(request, riskDto);
        Requests savedRequest = requestsRepository.save(request);

        Map<String, Object> processVars = new HashMap<>();
        processVars.put(ConstantString.MANAGER, savedRequest.getRiskManagerCode());
        processVars.put(ConstantString.OWNER, savedRequest.getRiskOwnerCode());
        Task task = camundaUtil.startProcess(processVars);

        commonUtil.preparedProcessInfo(savedRequest, task);
        commonUtil.preparedRequestHistory(savedRequest.getId(), riskDto.getNotes());
        commonUtil.addNewRequestSla(savedRequest.getId(), task.getId(), task.getAssignee());
        return "process started";
    }

    private void validateValidRisk(Long riskId) throws AppIllegalStateException {
        List<Requests> requestList = requestsRepository.findByRiskId(riskId);
        if (requestList != null && !requestList.isEmpty()) {
            ArrayList<String> endingStepList = new ArrayList<>(List.of(CamundaSteps.CONSTANT_DANGER.getValue(),
                    CamundaSteps.CLOSED_DANGER.getValue(), CamundaSteps.RISK_REJECTION.getValue()));
            for (Requests request : requestList) {
                if (!endingStepList.contains(request.getServiceStep().getStepCode())) {
                    throw new AppIllegalStateException("REQUEST_WITH_SAME_ID_IS_IN_PROGRESS");
                }
            }
        }
    }


    public RequestDto getRiskProcess(RiskDto riskDto) throws NoDataFoundException {

        Requests request = requestsRepository.findByRiskIdAndRiskOwnerIdAndRiskManagerId(riskDto.getRiskId(), riskDto.getRiskOwnerId(), riskDto.getRiskManagerId());
        if (request == null) {
            throw new NoDataFoundException(ConstantString.NO_DATA_FOUND);
        }
        ProcessInfo processInfo = request.getProcessInfoList().get(0);
        if (processInfo.getTaskId() == null) {
            throw new NoDataFoundException(ConstantString.NO_DATA_FOUND);
        }
        camundaUtil.validateTaskIdAndAssignee(processInfo, riskDto.getCurrentRole());
        return requestMapper.toRequestDto(request);
    }

    @Transactional(rollbackFor = Exception.class)
    public String completeRiskProcess(CompleteDto completeDto) throws NoDataFoundException, AppIllegalStateException {

        ArrayList<String> endingStepList = new ArrayList<>(List.of(CamundaSteps.CONSTANT_DANGER.getValue(),
                CamundaSteps.CLOSED_DANGER.getValue(), CamundaSteps.RISK_REJECTION.getValue()));

        camundaUtil.validateTaskHandling(completeDto.getTaskId());

        Requests request = camundaUtil.validateRequestDataForComplete(endingStepList, completeDto);
        ServiceSteps currentStep = request.getServiceStep();
        String nextStep = camundaUtil.getProcessNextStep(request.getServiceStep().getStepCode(), completeDto.getAction(), ConstantString.RISK_RULES_DESICION);
        request.setServiceStepId(serviceStepsRepository.findServiceStepsByStepCode(nextStep).getId());

        if (completeDto.getAction().equals("TRANSFER")) {
            request.setRiskManagerCode(completeDto.getNewRiskManagerCode());
            request.setRiskOwnerCode(completeDto.getNewRiskOwnerCode());
        }
        requestsRepository.save(request);

        Map<String, Object> vars = new HashMap<>();
        vars.put(ConstantString.ACTION, completeDto.getAction());
        if (completeDto.getAction().equals("TRANSFER")) {
            vars.put(ConstantString.MANAGER, completeDto.getNewRiskManagerCode());
            vars.put(ConstantString.OWNER, completeDto.getNewRiskOwnerCode());
        }

        ProcessInfo processInfo = request.getProcessInfoList().get(0);
        String currentTaskAssignee = request.getProcessInfoList().get(0).getTaskAssignee();
        camundaUtil.completeProcessTask(completeDto.getTaskId(),
                processInfo, endingStepList, vars, nextStep);

        commonUtil.preparedRequestHistory(request.getId(), currentTaskAssignee
                , completeDto.getNotes(), currentStep.getId(), completeDto.getAction());

        commonUtil.updateRequestSla(completeDto, currentTaskAssignee);
        if (!endingStepList.contains(nextStep)) {
            commonUtil.addNewRequestSla(completeDto.getRequestId(), processInfo.getTaskId(), processInfo.getTaskAssignee());
        }
        processInfoRepository.save(processInfo);
        return "process was sent to next task";
    }

}
