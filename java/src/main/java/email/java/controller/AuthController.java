package email.java.controller;

import email.java.model.User;
import email.java.service.MyUserDetalsService;
import email.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login"; // Retorna o nome do template Thymeleaf
    }

    @Autowired
    private MyUserDetalsService myUserDetailsService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/home";
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            model.addAttribute("error", "Email ou senha incorretos.");
            return "login";
        }
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Este e-mail já está cadastrado.");
            return "register";
        }

        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("registerSuccess", "Registro realizado com sucesso!"); // Mensagem de sucesso
        return "redirect:/auth/login";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model) {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "As senhas não coincidem.");
            return "reset-password";
        }

        boolean isUpdated = userService.resetPassword(email, newPassword);

        if (isUpdated) {
            model.addAttribute("success", "Senha atualizada com sucesso!");
            return "redirect:/auth/login";
        } else {
            model.addAttribute("error", "Erro ao atualizar a senha. Verifique o email.");
            return "reset-password";
        }
    }
}
