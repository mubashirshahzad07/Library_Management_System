package library.management.system.model;

public class Fine {
        
    private int fineId;
    private int transactionId;
    private int userId;
    private double fineAmount;
    private String paymentStatus;
    
    public Fine(int fineId, int transactionId, int userId, double fineAmount, String paymentStatus) {
        this.fineId = fineId;
        this.transactionId = transactionId;
        this.userId = userId;
        this.fineAmount = fineAmount;
        this.paymentStatus = paymentStatus;
    }
    
    public int getFineId() {
        return fineId;
    }
    
    public int getTransactionId() {
        return transactionId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public double getFineAmount() {
        return fineAmount;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
}
