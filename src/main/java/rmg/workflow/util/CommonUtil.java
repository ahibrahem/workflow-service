package rmg.workflow.util;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import rmg.workflow.exceptions.AppIllegalStateException;
import rmg.workflow.exceptions.NoDataFoundException;
import rmg.workflow.model.dto.CompleteDto;
import rmg.workflow.model.dto.RiskDto;
import rmg.workflow.model.entity.*;
import rmg.workflow.repository.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonUtil {

    private final ProcessInfoRepository processInfoRepository;
    private final RequestHistoryRepository requestHistoryRepository;
    private final ServiceStepActionsRepository serviceStepActionsRepository;
    private final RequestSlaRepository requestSlaRepository;
    private final UsersRepository usersRepository;

    public void preparedRequestData(Requests request, RiskDto riskDto) {
        request.setRiskId(riskDto.getRiskId());
        request.setRiskManagerId(riskDto.getRiskManagerId());
        request.setRiskOwnerId(riskDto.getRiskOwnerId());
        request.setPlanId(riskDto.getPlanId());
    }

    public void preparedProcessInfo(Requests request, Task task) throws NoDataFoundException {
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setRequestId(request.getRequestId());
        processInfo.setTaskId(task.getId());
        processInfo.setAssigneeUserId(findUserIdByRoleCode(task.getAssignee()));
        processInfo.setProcessInstance(task.getProcessInstanceId());
        processInfoRepository.save(processInfo);
    }

    public void preparedRequestHistory(UUID requestId, UUID assigneeUserId, String notes, UUID stepId, String actionCode) {
        RequestHistory requestHistory = new RequestHistory();
        requestHistory.setRequestId(requestId);
        requestHistory.setAssigneeUserId(assigneeUserId);
        requestHistory.setActionDate(LocalDateTime.now());
        requestHistory.setStepActionId(findStepAction(stepId, actionCode));
        requestHistory.setNotes(notes);
        requestHistoryRepository.save(requestHistory);
    }

    public UUID findStepAction(UUID stepId, String actionCode) {
        ServiceStepActions serviceStepActions = serviceStepActionsRepository.findByStepIdAndActionCode(stepId, actionCode);
        if (serviceStepActions != null) {
            return serviceStepActions.getId();
        }
        return null;
    }


    public void updateRequestSla(CompleteDto completeDto, UUID assigneeUser) {
        RequestSla requestSla = requestSlaRepository.findByRequestIdAndAssigneeUserAndTaskIdAndActionDateIsNull(completeDto.getRequestId(), assigneeUser, completeDto.getTaskId());
        requestSla.setActionUser(completeDto.getUserId());
        requestSla.setActionDate(LocalDateTime.now());
        requestSlaRepository.save(requestSla);

    }

    public void addNewRequestSla(UUID requestId, String taskId, UUID assigneeUser) {
        RequestSla requestSla = new RequestSla();
        requestSla.setRequestId(requestId);
        requestSla.setAssignDate(LocalDateTime.now());
        requestSla.setTaskId(taskId);
        requestSla.setAssigneeUser(assigneeUser);
        requestSlaRepository.save(requestSla);
    }


    public String findRoleCodeByUserId(UUID userId) throws NoDataFoundException {
        Users user = usersRepository.findUserById(userId);
        if (user == null) {
            throw new NoDataFoundException("USER_NOT_FOUND");
        }
        return user.getRoleCode();
    }

    public UUID findUserIdByRoleCode(String roleCode) throws NoDataFoundException {
        Users user = usersRepository.findByRoleCode(roleCode);
        if (user == null) {
            throw new NoDataFoundException("USER_NOT_FOUND");
        }
        return user.getId();
    }


    public void validateTransferActionNewRole(CompleteDto completeDto) throws AppIllegalStateException {
        if (completeDto.getAction().equals("TRANSFER")
                && (completeDto.getNewRiskManagerId() == null || completeDto.getNewRiskOwnerId() == null)) {
            throw new AppIllegalStateException("NEW_MANAGER_OR_NEW_OWNER_CANT_NULL");
        }
    }
}
