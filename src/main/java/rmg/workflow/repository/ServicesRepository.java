package rmg.workflow.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.Services;

import java.util.UUID;

@Repository
public interface ServicesRepository extends JpaRepository<Services, UUID> {

    Services findServicesByServiceCode(String serviceCode);
}
