package rmg.workflow.util;


import lombok.RequiredArgsConstructor;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import rmg.workflow.enumeration.CamundaProcess;
import rmg.workflow.exceptions.AppIllegalStateException;
import rmg.workflow.exceptions.NoDataFoundException;
import rmg.workflow.model.entity.ProcessInfo;
import rmg.workflow.model.entity.Requests;
import rmg.workflow.model.entity.ServiceSteps;
import rmg.workflow.model.entity.Services;
import rmg.workflow.repository.ServiceStepsRepository;
import rmg.workflow.repository.ServicesRepository;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CamundaUtil {

    private final ServicesRepository servicesRepository;
    private final ServiceStepsRepository serviceStepsRepository;

    public String getProcessNextStep(String currentStep, String currentAction, String dmnTableName) throws NoDataFoundException {

        DecisionService decisionService = ProcessEngines.getDefaultProcessEngine().getDecisionService();
        VariableMap variables = Variables.createVariables();
        variables.put(ConstantString.CURRENT_STEP, Variables.stringValue(currentStep));
        variables.put(ConstantString.ACTION_CODE, Variables.stringValue(currentAction));
        DmnDecisionRuleResult dmnDecisionRuleResult= decisionService.evaluateDecisionTableByKey(dmnTableName, variables).getSingleResult();
        if(dmnDecisionRuleResult == null){
            throw new NoDataFoundException("NO_DATA_FOUND_FOR_ACTION_INPUT");
        }
        Map<String, Object> businessRulesResultMap = dmnDecisionRuleResult.getEntryMap();
        return (String) businessRulesResultMap.get(ConstantString.NEXT_STEP);

    }

    public Task startProcess(Map<String, Object> processVars) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstanceWithVariables instance = processEngine.getRuntimeService()
                .createProcessInstanceByKey(CamundaProcess.RISK.getValue())
                .setVariables(processVars)
                .executeWithVariablesInReturn();

        return processEngine.getTaskService().createTaskQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();

    }


    public Requests initRequestObject(String serviceCode, String serviceStepCode, Long id) {
        Services services = servicesRepository.findServicesByServiceCode(serviceCode);
        ServiceSteps serviceStep = serviceStepsRepository.findServiceStepsByServiceStepCode(serviceStepCode);

        Requests request = new Requests();
        request.setServiceId(services.getServiceId());
        request.setRequestNo(serviceCode + "_" + id);
        request.setServiceStepId(serviceStep.getServiceStepId());

        return request;
    }


    public ProcessInfo preparedProcessInfo(Requests request, Task task) {
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setRequestId(request.getRequestId());
        processInfo.setTaskId(task.getId());
        processInfo.setTaskAssignee(task.getAssignee());
        processInfo.setProcessInstance(task.getProcessInstanceId());

        return processInfo;
    }


    public void validateTaskHandling(String taskId) throws AppIllegalStateException {
        Task task = ProcessEngines.getDefaultProcessEngine().getTaskService().createTaskQuery().taskId(taskId).active().singleResult();
        if (task == null)
            throw new AppIllegalStateException("TASK_NOT_FOUND_ERROR");
    }


    public void completeProcessTask(String taskId, ProcessInfo processInfo
            , List<String> endingStepList
            , Map<String, Object> vars
            , String nextStepCode) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete(taskId, vars);

        Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(processInfo.getProcessInstance()).active().singleResult();

        if (endingStepList.contains(nextStepCode)) {
            processInfo.setTaskId(null);
            processInfo.setTaskAssignee(null);
        } else {
            processInfo.setTaskId(task.getId());
            processInfo.setTaskAssignee(task.getAssignee());
        }
    }
}
