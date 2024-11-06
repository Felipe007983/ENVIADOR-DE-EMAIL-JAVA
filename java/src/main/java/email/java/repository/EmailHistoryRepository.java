package email.java.repository;

import email.java.model.EmailHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface EmailHistoryRepository extends CrudRepository<EmailHistory, Long> {

    List<EmailHistory> findAll();
}
