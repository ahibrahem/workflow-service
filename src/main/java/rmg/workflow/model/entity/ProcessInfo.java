package rmg.workflow.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Entity
@Table(name = "process_info")
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

    @Column(name = "task_assignee")
    private String taskAssignee;

    @Column(name = "process_instance")
    private String processInstance;


}


