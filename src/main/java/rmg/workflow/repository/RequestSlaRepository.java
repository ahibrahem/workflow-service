package rmg.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.RequestSla;

@Repository
public interface RequestSlaRepository extends JpaRepository<RequestSla, Long> {

    RequestSla findByRequestIdAndAssigneeUserAndTaskIdAndActionDateIsNull(Long requestId, Long assigneeUser, String taskId);
}
