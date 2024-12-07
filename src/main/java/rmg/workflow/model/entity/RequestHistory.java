package rmg.workflow.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ACT_REQUEST_HISTORY")
@Getter
@Setter
public class RequestHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "step_action_id")
    private Long stepActionId;

    @JoinColumn(name = "step_action_id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ServiceStepActions stepAction;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    @Column(name = "assignee_user")
    private Long assigneeUserId;

    @JoinColumn(name = "assignee_user", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users assigneeUser;

    @Column(name = "notes")
    private String notes;
}
