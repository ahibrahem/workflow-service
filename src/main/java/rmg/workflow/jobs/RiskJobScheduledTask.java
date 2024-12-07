package rmg.workflow.jobs;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmg.workflow.model.entity.Requests;
import rmg.workflow.repository.RequestsRepository;
import rmg.workflow.service.emailservice.EmailService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskJobScheduledTask {

    private final EmailService emailService;
    private final RequestsRepository requestsRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void executeTask() {
        List<Requests> requestsList = requestsRepository.findLateRequests();
        System.out.println("Scheduled task running at: " + LocalDateTime.now());

        if (requestsList != null && !requestsList.isEmpty()) {
            for (Requests request : requestsList) {
                String recipientEmail = request.getProcessInfoList().get(0).getAssigneeUser().getEmail(); // "engahmed27m9@gmail.com";
                String subject = "Hello from Spring Boot";
                String content = "<p>Hello,</p><p>This is a test email sent from Spring Boot.</p>";
                try {
                    emailService.sendEmail(recipientEmail, subject, content);
                    System.out.println("Email sent successfully.");
                } catch (MessagingException | UnsupportedEncodingException e) {
                    System.out.println("Failed to send email. Error: " + e.getMessage());
                }
            }
        }
    }
}
