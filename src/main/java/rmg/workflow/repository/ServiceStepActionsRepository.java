package rmg.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.ServiceStepActions;

import java.util.UUID;

@Repository
public interface ServiceStepActionsRepository extends JpaRepository<ServiceStepActions, UUID> {

    ServiceStepActions findByStepIdAndActionCode(UUID stepId, String actionCode);
}
