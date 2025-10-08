package in.akash.Transcation_Management;

import in.akash.Transcation_Management.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BrevoTestRunner implements CommandLineRunner {

    @Autowired
    private EmailService emailService;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Replace with your own email to test
            String testRecipient = "your-email@example.com";
            String subject = "Test Email from Brevo API";
            String body = "<h1>This is a test email</h1><p>If you receive this, your Brevo API works!</p>";

            emailService.sendEmail(testRecipient, subject, body);

            System.out.println("✅ Test email sent successfully!");
        } catch (Exception e) {
            System.err.println("❌ Failed to send test email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
