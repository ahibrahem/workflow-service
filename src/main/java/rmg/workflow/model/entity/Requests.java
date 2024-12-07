package rmg.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;


@Data
@Entity
@Table(name = "ACT_REQUESTS")
@Getter
@Setter
public class Requests implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "service_step_id")
    private Long serviceStepId;

    @Column(name = "risk_owner_id")
    private Long riskOwnerId;

    @Column(name = "risk_manager_id")
    private Long riskManagerId;

    @Column(name = "risk_id")
    private Long riskId;

    @Column(name = "plan_id")
    private Long planId;

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

}
