package email.java.controller;

import email.java.model.EmailConfig;
import email.java.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class EmailConfigController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/email-config")
    public String showEmailConfigForm(Model model) {
        model.addAttribute("emailConfig", new EmailConfig());
        model.addAttribute("emailConfigs", emailService.getAllEmailConfigs()); // Carregar as configurações existentes
        return "email_config";
    }

    @PostMapping("/email-config")
    public String saveEmailConfig(@ModelAttribute EmailConfig emailConfig, Model model) {
        emailService.saveEmailConfig(emailConfig);
        model.addAttribute("success", "Configuração de email salva com sucesso!");
        model.addAttribute("emailConfigs", emailService.getAllEmailConfigs()); // Atualizar a lista após salvar
        return "email_config";
    }
}
