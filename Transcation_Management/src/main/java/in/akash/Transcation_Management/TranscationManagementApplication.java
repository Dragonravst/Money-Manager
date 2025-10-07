package in.akash.Transcation_Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TranscationManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscationManagementApplication.class, args);
	}

}
