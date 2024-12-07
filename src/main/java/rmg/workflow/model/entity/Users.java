package rmg.workflow.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "ACT_USERS")
@Getter
@Setter
public class Users implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name_ar")
    private String userNameAr;

    @Column(name = "user_name_en")
    private String userNameEn;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

}
