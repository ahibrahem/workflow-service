package rmg.workflow.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Entity
@Table(name = "ACT_SERVICE_STEP_ACTIONS")
@Getter
@Setter
public class ServiceStepActions implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "step_id")
    private Long stepId;
    @Column(name = "action_name_ar")
    private String actionNameAr;
    @Column(name = "action_name_en")
    private String actionNameEn;
    @Column(name = "action_code")
    private String actionCode;
    
}
