package email.java.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EmailHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // Campo que representa o endereço de email
    private LocalDateTime datadoenvio; // Data e hora do envio
    private String carteira; // Campo representando a "carteira" ou grupo de destinatários
    private String statusenvio; // Status do envio (Enviado, Erro, etc.)
    private String errorMessage; // Mensagem de erro caso o envio falhe

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDatadoenvio() {
        return datadoenvio;
    }

    public void setDatadoenvio(LocalDateTime datadoenvio) {
        this.datadoenvio = datadoenvio;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public String getStatusenvio() {
        return statusenvio;
    }

    public void setStatusenvio(String statusenvio) {
        this.statusenvio = statusenvio;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
