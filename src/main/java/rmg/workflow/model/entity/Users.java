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
@Table(name = "AbpUsers")
@Getter
@Setter
public class Users implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @UuidGenerator()
    private UUID id;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "NormalizedUserName")
    private String normalizedUserName;

    @Column(name = "Name")
    private String name;

    @Column(name = "Email")
    private String email;

}
