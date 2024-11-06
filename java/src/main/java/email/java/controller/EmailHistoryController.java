package email.java.controller;

import email.java.model.EmailHistory;
import email.java.service.EmailHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmailHistoryController {

    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping("/email-history")
    public String getEmailHistory(Model model) {
        List<EmailHistory> emailHistories = emailHistoryService.getAllEmailHistories();
        model.addAttribute("emailHistories", emailHistories);
        return "email_history";
    }
}
