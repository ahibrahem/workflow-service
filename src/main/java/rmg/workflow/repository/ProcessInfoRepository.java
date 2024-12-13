package rmg.workflow.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.ProcessInfo;

import java.util.UUID;

@Repository
public interface ProcessInfoRepository extends JpaRepository<ProcessInfo, UUID> {
}
