package rmg.workflow.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Entity
@Table(name = "ACT_PROCESS_INFO")
@Getter
@Setter
public class ProcessInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "assignee_user")
    private Long assigneeUserId;

    @JoinColumn(name = "assignee_user", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users assigneeUser;

    @Column(name = "process_instance")
    private String processInstance;


}


