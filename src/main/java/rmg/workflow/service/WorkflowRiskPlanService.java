package rmg.workflow.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import rmg.workflow.enumeration.CamundaProcess;
import rmg.workflow.enumeration.CamundaSteps;
import rmg.workflow.exceptions.AppIllegalStateException;
import rmg.workflow.exceptions.NoDataFoundException;
import rmg.workflow.mapper.RequestMapper;
import rmg.workflow.model.dto.CompleteDto;
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
public class WorkflowRiskPlanService {


    private final RequestsRepository requestsRepository;
    private final ServiceStepsRepository serviceStepsRepository;
    private final CamundaUtil camundaUtil;
    private final RequestMapper requestMapper;
    private final CommonUtil commonUtil;
    private final ProcessInfoRepository processInfoRepository;

    public String startRiskPlanProcess(RiskDto riskDto) throws NoDataFoundException, AppIllegalStateException {

        if (riskDto.getRiskId() == null || riskDto.getPlanId() == null) {
            throw new AppIllegalStateException("RISK_ID_OR_PLAN_ID_CANT_BE_NULL");
        }
        validateIfRiskRequestExist(riskDto);

        Requests request = camundaUtil.initRequestObject(ConstantString.RISK_REDUCTION_PLAN, CamundaSteps.MANAGER_RISK_REDUCTION_PLAN.getValue(), riskDto.getPlanId());
        commonUtil.preparedRequestData(request, riskDto);
        Requests savedRequest = requestsRepository.save(request);

        Map<String, Object> processVars = new HashMap<>();
        processVars.put(ConstantString.MANAGER, commonUtil.findRoleCodeByUserId(riskDto.getRiskManagerId()));
        processVars.put(ConstantString.OWNER, commonUtil.findRoleCodeByUserId(riskDto.getRiskOwnerId()));
        Task task = camundaUtil.startProcess(processVars, CamundaProcess.RISK_PLAN.getValue());

        ServiceSteps serviceStep = serviceStepsRepository.findServiceStepsByStepCode(CamundaSteps.INIT_REDUCTION_PLAN.getValue());
        commonUtil.preparedProcessInfo(savedRequest, task);
        commonUtil.preparedRequestHistory(savedRequest.getRequestId(), null, riskDto.getNotes(), serviceStep.getId(), CamundaSteps.INIT_REDUCTION_PLAN.getValue());
        UUID userId = commonUtil.findUserIdByRoleCode(task.getAssignee());
        commonUtil.addNewRequestSla(savedRequest.getRequestId(), task.getId(), userId);
        return "process started";
    }

    private void validateIfRiskRequestExist(RiskDto riskDto) throws AppIllegalStateException {
        List<Requests> requestList = requestsRepository.findByRiskIdAndPlanId(riskDto.getRiskId(),riskDto.getPlanId());
        if (requestList != null && !requestList.isEmpty()) {
            throw new AppIllegalStateException("RISK_ID_AND_PLAN_ID_ALREADY_EXIST");
        }
    }

    public Object getRiskPlanProcess(RiskDto riskDto) throws NoDataFoundException {
        Requests request = requestsRepository.findByRiskIdAndRiskOwnerIdAndRiskManagerIdAndPlanIdAndPlanIdIsNotNull(riskDto.getRiskId(), riskDto.getRiskOwnerId(), riskDto.getRiskManagerId(), riskDto.getPlanId());
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

    public Object completeRiskPlanProcess(CompleteDto completeDto) throws AppIllegalStateException, NoDataFoundException {
        ArrayList<String> endingStepList = new ArrayList<>(List.of(CamundaSteps.RISK_REDUCTION_PLAN_CLOSED.getValue()));
        camundaUtil.validateTaskHandling(completeDto.getTaskId());

        Requests request = camundaUtil.validateRequestDataForComplete(endingStepList, completeDto);
        ServiceSteps currentStep = request.getServiceStep();
        String nextStep = camundaUtil.getProcessNextStep(request.getServiceStep().getStepCode(), completeDto.getAction(), ConstantString.RISK_PLAN_RULES_DECISION);
        request.setServiceStepId(serviceStepsRepository.findServiceStepsByStepCode(nextStep).getId());

        Map<String, Object> vars = new HashMap<>();
        vars.put(ConstantString.ACTION, completeDto.getAction());
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
}
