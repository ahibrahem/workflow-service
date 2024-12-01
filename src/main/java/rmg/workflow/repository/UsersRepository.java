package rmg.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmg.workflow.model.entity.Users;
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}
