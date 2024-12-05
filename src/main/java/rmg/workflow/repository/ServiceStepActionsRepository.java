package rmg.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.ServiceStepActions;

@Repository
public interface ServiceStepActionsRepository extends JpaRepository<ServiceStepActions, Long> {

    ServiceStepActions findByStepIdAndActionCode(Long stepId, String actionCode);
}
