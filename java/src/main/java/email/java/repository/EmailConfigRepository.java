package email.java.repository;

import email.java.model.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long> {
}