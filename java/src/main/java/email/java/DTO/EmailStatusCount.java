package email.java.DTO;

public class EmailStatusCount {
    private String carteira;
    private long totalSent;
    private long totalErrors;

    public EmailStatusCount(String carteira, long totalSent, long totalErrors) {
        this.carteira = carteira;
        this.totalSent = totalSent;
        this.totalErrors = totalErrors;
    }

    // Getters e Setters

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public long getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(long totalSent) {
        this.totalSent = totalSent;
    }

    public long getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(long totalErrors) {
        this.totalErrors = totalErrors;
    }
}
