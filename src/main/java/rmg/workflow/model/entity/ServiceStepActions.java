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
@Table(name = "ACT_SERVICE_STEP_ACTIONS")
@Getter
@Setter
public class ServiceStepActions implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @UuidGenerator()
    private UUID id;
    @Column(name = "step_id")
    private UUID stepId;
    @Column(name = "action_name_ar")
    private String actionNameAr;
    @Column(name = "action_name_en")
    private String actionNameEn;
    @Column(name = "action_code")
    private String actionCode;

}
