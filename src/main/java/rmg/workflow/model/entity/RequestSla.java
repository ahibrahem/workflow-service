package rmg.workflow.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ACT_REQUEST_SLA")
@Getter
@Setter
public class RequestSla implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "task_assignee")
    private String taskAssignee;

    @Column(name = "assign_date")
    private LocalDateTime assignDate;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    @Column(name = "action_user")
    private Long actionUser;

    @Column(name = "task_id")
    private String taskId;
}
