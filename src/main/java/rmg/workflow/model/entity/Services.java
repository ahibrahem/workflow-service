package rmg.workflow.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "services")
@Getter
@Setter
public class Services implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "service_code")
    private String serviceCode;
}
