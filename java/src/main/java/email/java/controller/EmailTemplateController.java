package email.java.controller;

import email.java.model.EmailTemplate;
import email.java.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class EmailTemplateController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/email-template")
    public String showEmailTemplates(Model model) {
        // Acessa a lista de templates de email através do serviço
        List<EmailTemplate> emailTemplates = emailService.getAllEmailTemplates(); // Atualizado para usar o método correto
        model.addAttribute("emailTemplates", emailTemplates);
        return "email_template"; // Nome da página de template de email
    }


    // Salva o template usando o serviço
    @PostMapping("/email-template")
    public String saveEmailTemplate(@ModelAttribute EmailTemplate emailTemplate, Model model) {
        emailService.saveEmailTemplate(emailTemplate);
        model.addAttribute("success", "Template de email salvo com sucesso!");
        return "redirect:/email-template";
    }
}
