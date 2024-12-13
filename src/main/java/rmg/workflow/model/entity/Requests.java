package rmg.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Table(name = "ACT_REQUESTS")
@Getter
@Setter
public class Requests implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @UuidGenerator()
    private UUID requestId;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "service_step_id")
    private UUID serviceStepId;

    @Column(name = "risk_owner_id")
    private UUID riskOwnerId;

    @Column(name = "risk_manager_id")
    private UUID riskManagerId;

    @Column(name = "risk_id")
    private UUID riskId;

    @Column(name = "plan_id")
    private UUID planId;

    @Column(name = "request_no")
    private String requestNo;

    @JsonIgnore
    @JoinColumn(name = "service_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Services service;

    //@JsonIgnore
    @JoinColumn(name = "service_step_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ServiceSteps serviceStep;

    @OneToMany(mappedBy = "requestId", fetch = FetchType.LAZY)
    private List<ProcessInfo> processInfoList;

    @OneToMany(mappedBy = "requestId", fetch = FetchType.LAZY)
    private List<RequestHistory> requestHistoryList;

    @OneToMany(mappedBy = "requestId", fetch = FetchType.LAZY)
    private List<RequestSla> requestSlaList;

}
