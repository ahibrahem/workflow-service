package rmg.workflow.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "ACT_REQUEST_SLA")
@Getter
@Setter
public class RequestSla implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @UuidGenerator()
    private UUID id;

    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "assignee_user")
    private UUID assigneeUser;

    @Column(name = "action_user")
    private UUID actionUser;

    @Column(name = "assign_date")
    private LocalDateTime assignDate;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

}
