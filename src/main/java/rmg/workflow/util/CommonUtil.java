package rmg.workflow.util;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import rmg.workflow.model.dto.CompleteDto;
import rmg.workflow.model.dto.RiskDto;
import rmg.workflow.model.entity.*;
import rmg.workflow.repository.ProcessInfoRepository;
import rmg.workflow.repository.RequestHistoryRepository;
import rmg.workflow.repository.RequestSlaRepository;
import rmg.workflow.repository.ServiceStepActionsRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommonUtil {

    private final ProcessInfoRepository processInfoRepository;
    private final RequestHistoryRepository requestHistoryRepository;
    private final ServiceStepActionsRepository serviceStepActionsRepository;
    private final RequestSlaRepository requestSlaRepository;

    public void preparedRequestData(Requests request, RiskDto riskDto) {
        request.setRiskId(riskDto.getRiskId());
        request.setRiskManagerId(riskDto.getRiskManagerId());
        request.setRiskManagerCode(riskDto.getRiskManagerCode());
        request.setRiskOwnerId(riskDto.getRiskOwnerId());
        request.setRiskOwnerCode(riskDto.getRiskOwnerCode());
    }

    public void preparedProcessInfo(Requests request, Task task) {
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setRequestId(request.getId());
        processInfo.setTaskId(task.getId());
        processInfo.setTaskAssignee(task.getAssignee());
        processInfo.setProcessInstance(task.getProcessInstanceId());
        processInfoRepository.save(processInfo);
    }

    public void preparedRequestHistory(Long requestId, String notes) {
        RequestHistory requestHistory = new RequestHistory();
        requestHistory.setRequestId(requestId);
        requestHistory.setActionDate(LocalDateTime.now());
        requestHistory.setNotes(notes);
        requestHistoryRepository.save(requestHistory);
    }

    public void preparedRequestHistory(Long requestId, String taskAssignee, String notes, Long stepId, String actionCode) {
        RequestHistory requestHistory = new RequestHistory();
        requestHistory.setRequestId(requestId);
        requestHistory.setTaskAssignee(taskAssignee);
        requestHistory.setActionDate(LocalDateTime.now());
        requestHistory.setStepActionId(findStepAction(stepId, actionCode));
        requestHistory.setNotes(notes);
        requestHistoryRepository.save(requestHistory);
    }

    public Long findStepAction(Long stepId, String actionCode) {
        ServiceStepActions serviceStepActions = serviceStepActionsRepository.findByStepIdAndActionCode(stepId, actionCode);
        if (serviceStepActions != null) {
            return serviceStepActions.getId();
        }
        return null;
    }


    public void updateRequestSla(CompleteDto completeDto , String taskAssignee) {
        RequestSla requestSla =  requestSlaRepository.findByRequestIdAndTaskAssigneeAndTaskIdAndActionDateIsNull(completeDto.getRequestId(),taskAssignee,completeDto.getTaskId());
        requestSla.setActionUser(completeDto.getUserId());
        requestSla.setActionDate(LocalDateTime.now());
        requestSlaRepository.save(requestSla);

    }

    public void addNewRequestSla(Long requestId, String taskId , String taskAssignee) {
        RequestSla requestSla = new RequestSla();
        requestSla.setRequestId(requestId);
        requestSla.setAssignDate(LocalDateTime.now());
        requestSla.setTaskId(taskId);
        requestSla.setTaskAssignee(taskAssignee);
        requestSlaRepository.save(requestSla);
    }


}
