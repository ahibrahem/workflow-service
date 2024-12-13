package rmg.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.ServiceSteps;
import rmg.workflow.model.entity.Services;

import java.util.UUID;

@Repository
public interface ServiceStepsRepository extends JpaRepository<ServiceSteps, UUID> {

    ServiceSteps findServiceStepsByStepCode(String serviceStepCode);
}
