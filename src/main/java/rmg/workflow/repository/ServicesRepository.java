package rmg.workflow.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.Services;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {

    Services findServicesByServiceCode(String serviceCode);
}
