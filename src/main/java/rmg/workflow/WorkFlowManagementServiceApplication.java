package rmg.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = "rmg.workflow")
@EnableScheduling
public class WorkFlowManagementServiceApplication {

  public static void main(String... args) {
    SpringApplication.run(WorkFlowManagementServiceApplication.class, args);
  }

}