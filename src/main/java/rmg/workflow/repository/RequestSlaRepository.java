package rmg.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.RequestSla;

import java.util.UUID;

@Repository
public interface RequestSlaRepository extends JpaRepository<RequestSla, UUID> {

    RequestSla findByRequestIdAndAssigneeUserAndTaskIdAndActionDateIsNull(UUID requestId, UUID assigneeUser, String taskId);
}
