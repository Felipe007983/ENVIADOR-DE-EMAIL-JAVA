package email.java.security;

import email.java.service.MyUserDetalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetalsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Ignora a proteção CSRF para as requisições que começam com "/api/"
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                // Configurações de autorização
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/login", "/auth/register", "/auth/reset-password").permitAll() // Permite acesso sem autenticação
                        .anyRequest().authenticated() // Requer autenticação para todas as outras requisições
                )
                // Configuração de login
                .formLogin(form -> form
                        .loginPage("/auth/login") // Página de login personalizada
                        .usernameParameter("email") // Usa 'email' como parâmetro de nome de usuário
                        .defaultSuccessUrl("/home", true) // Redireciona para '/home' após login bem-sucedido
                        .permitAll() // Permite acesso à página de login
                )
                // Configuração de logout
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // URL para logout
                        .logoutSuccessUrl("/auth/login?logout") // Redireciona após logout
                        .permitAll() // Permite acesso à ação de logout
                );

        return http.build(); // Retorna a configuração de segurança
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para codificação de senhas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Retorna o gerenciador de autenticação
    }
}
