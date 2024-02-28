package models;
/**
 *
 * @author Nick Giovanopoulos
 */
public class Review {

    private int reviewId;
    private int reservationId;
    private String comment;
    private int rating;

    public Review(String comment, int rating, int reservationId) {
        this.comment = comment;
        this.rating = rating;
        this.reservationId = reservationId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getComments() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public int getReviewId() {
        return reviewId;
    }

    public String getComment() {
        return comment;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}