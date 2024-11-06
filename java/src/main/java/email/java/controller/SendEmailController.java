package email.java.controller;

import email.java.DTO.EmailStatusCount;
import email.java.model.EmailHistory;
import email.java.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SendEmailController {

    @Autowired
    private EmailService emailService;

    // Exibir o formulário de envio de email
    @GetMapping("/send-email-form")
    public String showSendEmailForm(Model model) {
        // Adiciona as configurações e templates ao modelo
        model.addAttribute("configs", emailService.getAllEmailConfigs());
        model.addAttribute("templates", emailService.getAllEmailTemplates());
        return "send_email";
    }

    // Ação para enviar e-mails
    @PostMapping("/send-email-action")
    public String sendEmail(
            @RequestParam Long configId,
            @RequestParam Long templateId,
            @RequestParam int speed,
            @RequestParam("emailFile") MultipartFile emailFile,
            Model model
    ) {
        try {
            // Enviar emails usando os parâmetros fornecidos
            emailService.sendBulkEmails(configId, templateId, emailFile, speed);
            model.addAttribute("success", "Emails enviados com sucesso!");
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao enviar emails: " + e.getMessage());
        }
        // Recarrega a lista de configurações e templates para a página
        model.addAttribute("configs", emailService.getAllEmailConfigs());
        model.addAttribute("templates", emailService.getAllEmailTemplates());
        return "send_email";
    }

    // Exibir a página de envio de e-mails
    @GetMapping("/send-email")
    public String showSendEmailPage(Model model) {
        // Adiciona as configurações e templates ao modelo
        model.addAttribute("configs", emailService.getAllEmailConfigs());
        model.addAttribute("templates", emailService.getAllEmailTemplates());
        return "send_email";
    }



}
