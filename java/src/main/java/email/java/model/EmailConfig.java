package email.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmailConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String smtpServer;
    private int port;
    private String username;
    private String password;

    // Adicionando um campo para recipientEmail, caso queira mantê-lo aqui
    private String recipientEmail; // Adicione este campo, se necessário

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRecipientEmail() {
        return recipientEmail; // Adicione este método
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail; // Adicione este método
    }
}
