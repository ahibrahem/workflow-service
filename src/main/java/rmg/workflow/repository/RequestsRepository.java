package rmg.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.Requests;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestsRepository extends JpaRepository<Requests, Long> {

    Requests findByRiskIdAndRiskOwnerIdAndRiskManagerIdAndPlanIdIsNull(Long riskId, Long riskOwnerId, Long riskManagerId);
    Requests findByRiskIdAndRiskOwnerIdAndRiskManagerIdAndPlanIdAndPlanIdIsNotNull(Long riskId, Long riskOwnerId, Long riskManagerId , Long planId);

    List<Requests> findByRiskId(Long riskId);

    @Query(
            value = "select req.* " +
                    "from [workflow].[dbo].[ACT_REQUESTS] req " +
                    "join [workflow].[dbo].[ACT_REQUEST_SLA] sla on req.id = sla.request_id " +
                    "where sla.action_date IS NULL and sla.action_user IS NULL " +
                    "and DATEADD(DAY, -2,CONVERT(VARCHAR(10), GETDATE(), 120)) >=  CONVERT(VARCHAR(10), sla.assign_date, 120)",
            nativeQuery = true
    )
    List<Requests> findLateRequests();

}
