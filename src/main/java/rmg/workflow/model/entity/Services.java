package rmg.workflow.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "ACT_SERVICES")
@Getter
@Setter
public class Services implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @UuidGenerator()
    private UUID id;
    @Column(name = "service_name_ar")
    private String serviceNameAr;
    @Column(name = "service_name_en")
    private String serviceNameEn;
    @Column(name = "service_code")
    private String serviceCode;
}
