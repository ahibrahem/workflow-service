package rmg.workflow.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "service_steps")
@Getter
@Setter
public class ServiceSteps implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_step_id")
    private Long serviceStepId;
    @Column(name = "service_step_name")
    private String serviceStepName;
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "service_step_code")
    private String serviceStepCode;
}
