package email.java.controller;

import email.java.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/config-email")
    public String showConfigEmailPage(Model model) {
        model.addAttribute("emailConfigs", emailService.getAllEmailConfigs());
        return "config-email";
    }

    /*@GetMapping("/config-template")
    public String showConfigTemplatePage(Model model) {
        model.addAttribute("emailTemplates", emailService.getAllEmailTemplates());
        return "config-template";
    }

    @GetMapping("/send-email")
    public String showSendEmailPage(Model model) {
        model.addAttribute("emailConfigs", emailService.getAllEmailConfigs());
        model.addAttribute("emailTemplates", emailService.getAllEmailTemplates());
        return "send-email"; // Tela para enviar e-mail
    }

    @PostMapping("/send-email")
    public String sendEmail(
            @RequestParam Long emailConfigId,
            @RequestParam Long emailTemplateId,
            @RequestParam(required = false) String recipientOverride,
            Model model
    ) {
        emailService.sendEmail(emailConfigId, emailTemplateId, recipientOverride);
        model.addAttribute("success", "Email enviado com sucesso!");
        return "send-email"; // Ou redirecione para uma página de sucesso
    }*/
}
