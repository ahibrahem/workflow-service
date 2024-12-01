package rmg.workflow.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
@Getter
@Setter
public class Users implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

}
