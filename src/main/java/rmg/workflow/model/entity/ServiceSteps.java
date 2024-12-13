package rmg.workflow.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "ACT_SERVICE_STEPS")
@Getter
@Setter
public class ServiceSteps implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @UuidGenerator()
    private UUID id;
    @Column(name = "step_name_ar")
    private String stepNameAr;
    @Column(name = "step_name_en")
    private String stepNameEn;
    @Column(name = "service_id")
    private UUID serviceId;
    @Column(name = "step_code")
    private String stepCode;

    @OneToMany(mappedBy = "stepId", fetch = FetchType.LAZY)
    private List<ServiceStepActions> serviceStepActionList;
}
