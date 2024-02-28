package models;

/**
 *
 * @author Nick Giovanopoulos
 */
public class Transaction {
    private int transactionId;
    private String date;
    private float amount;
    private String paymentType;

    public Transaction(String date, float amount, String paymentType) {
        this.date = date;
        this.amount = amount;
        this.paymentType = paymentType;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
