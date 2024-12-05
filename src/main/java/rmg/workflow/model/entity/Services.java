package rmg.workflow.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "ACT_SERVICES")
@Getter
@Setter
public class Services implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "service_name_ar")
    private String serviceNameAr;
    @Column(name = "service_name_en")
    private String serviceNameEn;
    @Column(name = "service_code")
    private String serviceCode;
}
