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

    Requests findByRiskIdAndRiskOwnerIdAndRiskManagerId(Long riskId, Long riskOwnerId, Long riskManagerId);

    List<Requests> findByRiskId(Long riskId);

}
