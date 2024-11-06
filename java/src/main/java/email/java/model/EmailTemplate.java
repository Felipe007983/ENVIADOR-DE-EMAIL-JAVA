package email.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title; // Adicione este método
    }

    public void setTitle(String title) {
        this.title = title; // Adicione este método
    }

    public String getContent() {
        return content; // Adicione este método
    }

    public void setContent(String content) {
        this.content = content; // Adicione este método
    }
}
