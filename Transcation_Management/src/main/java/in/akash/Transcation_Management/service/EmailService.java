package in.akash.Transcation_Management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${BREVO_FROM_EMAIL}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String htmlBody) {
        String url = "https://api.brevo.com/v3/smtp/email";

        Map<String, Object> payload = Map.of(
                "sender", Map.of("email", fromEmail),
                "to", List.of(Map.of("email", to)),
                "subject", subject,
                "htmlContent", htmlBody
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("Failed to send email. Response: " + response.getBody());
            } else {
                System.out.println("Email sent successfully to " + to);
            }
        } catch (RestClientException e) {
            System.err.println("Error sending email to " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
