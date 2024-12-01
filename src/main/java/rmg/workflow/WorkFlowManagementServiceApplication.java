package rmg.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "rmg.workflow")
public class WorkFlowManagementServiceApplication {

  public static void main(String... args) {
    SpringApplication.run(WorkFlowManagementServiceApplication.class, args);
  }

}