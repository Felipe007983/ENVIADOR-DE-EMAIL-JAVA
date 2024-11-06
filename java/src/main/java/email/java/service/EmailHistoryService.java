package email.java.service;

import email.java.model.EmailHistory;
import email.java.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailHistoryService {

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public List<EmailHistory> getAllEmailHistories() {
        return emailHistoryRepository.findAll();
    }
}
