package models;

/**
 *
 * @author Nick Giovanopoulos
 */
public class Reservation {

    private int reservationId;
    private int roomId;
    private int transactionId;
    private int costumerId;
    private String date;
    private int slot;
    private String state;
    public Reservation(int roomId, int costumerId, int transactionId, String date, int slot, String state) {
        this.roomId = roomId;
        this.costumerId = costumerId;
        this.date = date;
        this.transactionId = transactionId;
        this.slot = slot;
        this.state = state;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getCostumerId() {
        return costumerId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setCostumerId(Costumer costumer) {
        this.costumerId = costumerId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setCostumerId(int costumerId) {
        this.costumerId = costumerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}