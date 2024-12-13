package rmg.workflow.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "ACT_PROCESS_INFO")
@Getter
@Setter
public class ProcessInfo implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @UuidGenerator()
    private UUID id;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "assignee_user")
    private UUID assigneeUserId;

    @JoinColumn(name = "assignee_user", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users assigneeUser;

    @Column(name = "process_instance")
    private String processInstance;


}


