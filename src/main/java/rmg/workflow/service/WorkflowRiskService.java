package rmg.workflow.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmg.workflow.enumeration.CamundaProcess;
import rmg.workflow.enumeration.CamundaSteps;
import rmg.workflow.exceptions.AppIllegalStateException;
import rmg.workflow.exceptions.NoDataFoundException;
import rmg.workflow.mapper.RequestMapper;
import rmg.workflow.model.dto.CompleteDto;
import rmg.workflow.model.dto.RequestDetailsDto;
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
public class WorkflowRiskService {

    private final RequestsRepository requestsRepository;
    private final ServiceStepsRepository serviceStepsRepository;
    private final CamundaUtil camundaUtil;
    private final RequestMapper requestMapper;
    private final CommonUtil commonUtil;
    private final ProcessInfoRepository processInfoRepository;


    @Transactional(rollbackFor = Exception.class)
    public String startProcess(RiskDto riskDto) throws AppIllegalStateException, NoDataFoundException {

        validateIfRiskRequestExist(riskDto.getRiskId());

        Requests request = camundaUtil.initRequestObject(ConstantString.RISK_SERVICE, CamundaSteps.OWNER_RISK_ANALYSIS.getValue(), riskDto.getRiskId());

        commonUtil.preparedRequestData(request, riskDto);
        Requests savedRequest = requestsRepository.save(request);

        Map<String, Object> processVars = new HashMap<>();
        processVars.put(ConstantString.MANAGER, commonUtil.findRoleCodeByUserId(riskDto.getRiskManagerId()));
        processVars.put(ConstantString.OWNER, commonUtil.findRoleCodeByUserId(riskDto.getRiskOwnerId()));
        Task task = camundaUtil.startProcess(processVars, CamundaProcess.RISK.getValue());

        ServiceSteps serviceStep = serviceStepsRepository.findServiceStepsByStepCode(CamundaSteps.INIT_RISK.getValue());
        commonUtil.preparedProcessInfo(savedRequest, task);
        commonUtil.preparedRequestHistory(savedRequest.getRequestId(), null, riskDto.getNotes(), serviceStep.getId(), CamundaSteps.INIT_RISK.getValue());
        UUID userId = commonUtil.findUserIdByRoleCode(task.getAssignee());
        commonUtil.addNewRequestSla(savedRequest.getRequestId(), task.getId(), userId);
        return "process started";
    }

    private void validateIfRiskRequestExist(UUID riskId) throws AppIllegalStateException {
        List<Requests> requestList = requestsRepository.findByRiskId(riskId);
        if (requestList != null && !requestList.isEmpty()) {
            throw new AppIllegalStateException("RISK_ID_ALREADY_EXIST");
        }
    }


    public RequestDto getRiskProcess(RiskDto riskDto) throws NoDataFoundException {

        Requests request = requestsRepository.findByRiskIdAndRiskOwnerIdAndRiskManagerIdAndPlanIdIsNull(riskDto.getRiskId(), riskDto.getRiskOwnerId(), riskDto.getRiskManagerId());
        if (request == null) {
            throw new NoDataFoundException(ConstantString.NO_DATA_FOUND);
        }
        ProcessInfo processInfo = request.getProcessInfoList().get(0);
        if (processInfo.getTaskId() == null) {
            throw new NoDataFoundException(ConstantString.NO_DATA_FOUND);
        }
        camundaUtil.validateTaskIdAndAssignee(processInfo, riskDto.getCurrentUser());
        return requestMapper.toRequestDto(request);
    }

    @Transactional(rollbackFor = Exception.class)
    public String completeRiskProcess(CompleteDto completeDto) throws NoDataFoundException, AppIllegalStateException {

        ArrayList<String> endingStepList = new ArrayList<>(List.of(CamundaSteps.CONSTANT_DANGER.getValue(),
                CamundaSteps.CLOSED_DANGER.getValue(), CamundaSteps.RISK_REJECTION.getValue()));

        camundaUtil.validateTaskHandling(completeDto.getTaskId());
        commonUtil.validateTransferActionNewRole(completeDto);

        Requests request = camundaUtil.validateRequestDataForComplete(endingStepList, completeDto);
        ServiceSteps currentStep = request.getServiceStep();
        String nextStep = camundaUtil.getProcessNextStep(request.getServiceStep().getStepCode(), completeDto.getAction(), ConstantString.RISK_RULES_DECISION);
        request.setServiceStepId(serviceStepsRepository.findServiceStepsByStepCode(nextStep).getId());

        if (completeDto.getAction().equals("TRANSFER")) {
            request.setRiskManagerId(completeDto.getNewRiskManagerId());
            request.setRiskOwnerId(completeDto.getNewRiskManagerId());
        }
        requestsRepository.save(request);

        Map<String, Object> vars = new HashMap<>();
        vars.put(ConstantString.ACTION, completeDto.getAction());
        if (completeDto.getAction().equals("TRANSFER")) {
            vars.put(ConstantString.MANAGER, commonUtil.findRoleCodeByUserId(completeDto.getNewRiskManagerId()));
            vars.put(ConstantString.OWNER, commonUtil.findRoleCodeByUserId(completeDto.getNewRiskOwnerId()));
        }

        ProcessInfo processInfo = request.getProcessInfoList().get(0);
        UUID currentAssigneeUser = request.getProcessInfoList().get(0).getAssigneeUserId();
        camundaUtil.completeProcessTask(completeDto.getTaskId(),
                processInfo, endingStepList, vars, nextStep);

        commonUtil.preparedRequestHistory(request.getRequestId(), currentAssigneeUser
                , completeDto.getNotes(), currentStep.getId(), completeDto.getAction());

        commonUtil.updateRequestSla(completeDto, currentAssigneeUser);
        if (!endingStepList.contains(nextStep)) {
            commonUtil.addNewRequestSla(completeDto.getRequestId(), processInfo.getTaskId(), processInfo.getAssigneeUserId());
        }
        processInfoRepository.save(processInfo);
        return "process was sent to next task";
    }

    public RequestDetailsDto getRiskDetails(UUID riskId) throws NoDataFoundException {
        List<Requests> requestList = requestsRepository.findByRiskId(riskId);
        if (requestList == null || requestList.isEmpty()) {
            throw new NoDataFoundException(ConstantString.NO_DATA_FOUND);
        }
        return requestMapper.toRequestDetailsDto(requestList.get(0));
    }
}
