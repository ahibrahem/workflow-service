package rmg.workflow.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.ProcessInfo;

@Repository
public interface ProcessInfoRepository extends JpaRepository<ProcessInfo, Long> {
}
