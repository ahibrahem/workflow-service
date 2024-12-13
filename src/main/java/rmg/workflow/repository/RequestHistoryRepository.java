package rmg.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.RequestHistory;

import java.util.UUID;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory, UUID> {
}
