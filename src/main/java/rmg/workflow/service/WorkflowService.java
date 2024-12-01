package rmg.workflow.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
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
import rmg.workflow.repository.ProcessInfoRepository;
import rmg.workflow.repository.RequestsRepository;
import rmg.workflow.repository.ServiceStepsRepository;
import rmg.workflow.util.CamundaUtil;
import rmg.workflow.util.ConstantString;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final RequestsRepository requestsRepository;
    private final ServiceStepsRepository serviceStepsRepository;
    private final ProcessInfoRepository processInfoRepository;
    private final CamundaUtil camundaUtil;
    private final RequestMapper requestMapper;


    @Transactional(rollbackFor = Exception.class)
    public String startProcess(RiskDto riskDto) throws AppIllegalStateException {

        validateValidRisk(riskDto.getRiskId());

        Requests request = camundaUtil.initRequestObject(ConstantString.RISK_SERVICE, CamundaSteps.OWNER_RISK_ANALYSIS.getValue(), riskDto.getRiskId());

        preparedRequestData(request, riskDto);
        Requests savedRequest = requestsRepository.save(request);

        Map<String, Object> processVars = new HashMap<>();
        processVars.put("manager", savedRequest.getRiskManagerCode());
        processVars.put("owner", savedRequest.getRiskOwnerCode());
        Task task = camundaUtil.startProcess(processVars);

        ProcessInfo processInfo = camundaUtil.preparedProcessInfo(savedRequest, task);
        processInfoRepository.save(processInfo);

        return "process started";
    }

    private void validateValidRisk(Long riskId) throws AppIllegalStateException {
        List<Requests> requestList = requestsRepository.findByRiskId(riskId);
        if (requestList != null && !requestList.isEmpty()) {
            ArrayList<String> endingStepList = new ArrayList<>(List.of(CamundaSteps.CONSTANT_DANGER.getValue(),
                    CamundaSteps.CLOSED_DANGER.getValue(), CamundaSteps.RISK_REJECTION.getValue()));
            for (Requests request : requestList) {
                if (!endingStepList.contains(request.getServiceStep().getServiceStepCode())) {
                    throw new AppIllegalStateException("REQUEST_WITH_SAME_ID_IS_IN_PROGRESS");
                }
            }
        }
    }

    private void preparedRequestData(Requests request, RiskDto riskDto) {
        request.setRiskId(riskDto.getRiskId());
        request.setRiskManagerId(riskDto.getRiskManagerId());
        request.setRiskManagerCode(riskDto.getRiskManagerCode());
        request.setRiskOwnerId(riskDto.getRiskOwnerId());
        request.setRiskOwnerCode(riskDto.getRiskOwnerCode());
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
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Task task = processEngine.getTaskService().createTaskQuery().taskId(processInfo.getTaskId()).active().singleResult();
        if (task == null || !processInfo.getTaskAssignee().equals(riskDto.getCurrentRole())) {
            throw new NoDataFoundException(ConstantString.NO_DATA_FOUND);
        }
        return requestMapper.toRequestDto(request);
    }

    @Transactional(rollbackFor = Exception.class)
    public String completeRiskProcess(CompleteDto completeDto) throws NoDataFoundException, AppIllegalStateException {

        ArrayList<String> endingStepList = new ArrayList<>(List.of(CamundaSteps.CONSTANT_DANGER.getValue(),
                CamundaSteps.CLOSED_DANGER.getValue(), CamundaSteps.RISK_REJECTION.getValue()));

        camundaUtil.validateTaskHandling(completeDto.getTaskId());
        Optional<Requests> requestsOptional = requestsRepository.findById(completeDto.getRequestId());
        if (requestsOptional.isEmpty()) {
            throw new NoDataFoundException("REQUEST_NOT_FOUND");
        }
        Requests request = requestsOptional.get();

        if (!completeDto.getTaskId().equals(request.getProcessInfoList().get(0).getTaskId())) {
            throw new NoDataFoundException("INVALID_REQUEST_ID_OR_TASK_ID");
        }

        if (endingStepList.contains(request.getServiceStep().getServiceStepCode())) {
            throw new NoDataFoundException("INVALID_REQUEST_ID");
        }

        String nextStep = camundaUtil.getProcessNextStep(request.getServiceStep().getServiceStepCode(), completeDto.getAction(), ConstantString.RISK_RULES_DESICION);
        request.setServiceStepId(serviceStepsRepository.findServiceStepsByServiceStepCode(nextStep).getServiceStepId());
        requestsRepository.save(request);

        Map<String, Object> vars = new HashMap<>();
        vars.put(ConstantString.ACTION, completeDto.getAction());

        ProcessInfo processInfo = request.getProcessInfoList().get(0);
        camundaUtil.completeProcessTask(completeDto.getTaskId(),
                processInfo, endingStepList, vars, nextStep);

        processInfoRepository.save(processInfo);
        return "process was sent to next task";
    }

}
