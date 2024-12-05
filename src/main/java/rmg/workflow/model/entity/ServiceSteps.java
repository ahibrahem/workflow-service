package rmg.workflow.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "ACT_SERVICE_STEPS")
@Getter
@Setter
public class ServiceSteps implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "step_name_ar")
    private String stepNameAr;
    @Column(name = "step_name_en")
    private String stepNameEn;
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "step_code")
    private String stepCode;

    @OneToMany(mappedBy = "stepId", fetch = FetchType.LAZY)
    private List<ServiceStepActions> serviceStepActionList;
}
