package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import models.Review;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
/**
 *
 * @author Nick Giovanopoulos
 */
public class ReviewTable {

    /**
     Creates the reviews table in the database.
     @throws SQLException if a database access error occurs.
     @throws ClassNotFoundException if the class cannot be found.
     */
    public void createReviewTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE reviews "
                + "(   reviewId INTEGER not NULL AUTO_INCREMENT,"
                + "    comment VARCHAR(200) not null,"
                + "    rating INTEGER not null,"
                + "    reservationId INTEGER not null,"
                + "    FOREIGN KEY (reservationId) REFERENCES reservations(reservationId), "
                + "    PRIMARY KEY (reviewId))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     Adds a review to the database.
     @param review the review object to add to the database.
     @throws ClassNotFoundException if the class cannot be found.
     */
    public void addReview(Review review) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO reviews (comment,rating,reservationId)"
                    + " VALUES ("
                    + "'" + review.getComment() + "',"
                    + "'" + review.getRating() + "',"
                    + "'" + review.getReservationId() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The review was successfully added in the database.");
            /* Get the member id from the database and set it to the member */
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReviewTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     Adds a review to the database for the given customer and room.
     @param costumerId the ID of the customer who left the review.
     @param roomId the ID of the room for which the review is being left.
     @param comment the comment left in the review.
     @param rating the rating given in the review.
     @return 0 if the review was added successfully, -1 if the customer cannot leave a review for the given room.
     @throws ClassNotFoundException if the class cannot be found.
     */
    public int addReview(int costumerId, int roomId, String comment, int rating) throws ClassNotFoundException {
        int result = -1;
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            // Get the reservation IDs for the costumer's completed reservations for the given room
            String reservationsQuery = "SELECT reservationId FROM reservations WHERE costumerId = " + costumerId
                    + " AND roomId = " + roomId
                    + " AND state = 'COMPLETED'";
            ResultSet reservationsResult = stmt.executeQuery(reservationsQuery);
            ArrayList<Integer> reservationIds = new ArrayList<>();
            while (reservationsResult.next()){
                reservationIds.add(reservationsResult.getInt("reservationId"));
            }
            if (reservationIds.isEmpty()) {
                // The costumer doesn't have any completed reservations for this room, so they can't leave a review
                System.out.println("# The costumer has no completed reservations for this room, so they cannot leave a review.");
                result = -1;
            } else {
                // Get the reservation IDs for the costumer's completed reservations for the given room that they haven't left a review for yet
                String reviewsQuery = "SELECT reservationId FROM reviews WHERE reservationId IN (" + String.join(",", reservationIds.stream().map(Object::toString).toArray(String[]::new)) + ")";
                ResultSet reviewsResult = stmt.executeQuery(reviewsQuery);
                Set<Integer> usedReservationIds = new HashSet<>();
                while (reviewsResult.next()) usedReservationIds.add(reviewsResult.getInt("reservationId"));
                List<Integer> availableReservationIds = reservationIds.stream().filter(r -> !usedReservationIds.contains(r)).collect(Collectors.toList());
                if (availableReservationIds.isEmpty()) {
                    // The costumer has left a review for all of their completed reservations for this room
                    System.out.println("# The costumer has already left a review for all of their completed reservations for this room.");
                    result = -1;
                } else {
                    // Insert the review with the lowest available reservation ID
                    int nextReservationId = Collections.min(availableReservationIds);
                    String insertQuery = "INSERT INTO reviews (comment, rating, reservationId)"
                            + " VALUES ("
                            + "'" + comment + "',"
                            + "'" + rating + "',"
                            + "'" + nextReservationId + "'"
                            + ")";
                    stmt.executeUpdate(insertQuery);
                    System.out.println("# The review was successfully added in the database with reservation ID " + nextReservationId + ".");
                    result = 0;
                }
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReviewTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     Returns a JSON string containing all the reviews for the given room.
     @param roomId the ID of the room for which to get the reviews.
     @return a JSON string containing all the reviews for the given room.
     @throws SQLException if a database access error occurs.
     @throws ClassNotFoundException if the class cannot be found.
     */
    public String databaseToReviews(int roomId) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JSONArray reviewsArray = new JSONArray();
        try {
            String query = "SELECT r.comment, r.rating, c.username, c.costumerId\n" +
                    "FROM reviews r, reservations res, costumers c, rooms rm\n" +
                    "WHERE r.reservationId = res.reservationId\n" +
                    "AND res.costumerId = c.costumerId\n" +
                    "AND res.roomId = rm.roomId\n" +
                    "AND rm.roomId = '"+roomId+"';";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String comment = rs.getString("comment");
                int rating = rs.getInt("rating");
                String username = rs.getString("username");
                int costumerId = rs.getInt("costumerId");
                // Create a new JSON object for the review
                JSONObject reviewObject = new JSONObject();
                reviewObject.put("comment", comment);
                reviewObject.put("rating", rating);
                reviewObject.put("username", username);
                reviewObject.put("costumerId", costumerId);
                // Add the JSON object to the reviews array
                reviewsArray.put(reviewObject);
            }
        } catch (Exception e) {
            System.err.println("databaseToReview: Got an exception! ");
            System.err.println(e.getMessage());
        }
        // Convert the reviews array to a JSON string
        return reviewsArray.toString();
    }


    /**
     Converts a Review object to a JSON string using the Gson library.
     @param review the Review object to be converted to a JSON string
     @return a JSON string representation of the Review object
     */
    public String reviewToJSON(Review review){
        Gson gson = new Gson();
        String json = gson.toJson(review, Review.class);
        return json;
    }

    /**
     Adds a Review object to the system by converting a JSON string to a Review object using the Gson library.
     @param json a JSON string representation of the Review object to be added
     @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    public void addReviewJSON(String json) throws ClassNotFoundException{
        Review review =jsonToReview(json);
        addReview(review);
    }

    /**
     Converts a JSON string to a Review object using the Gson library.
     @param json the JSON string representation of the Review object
     @return a Review object represented by the JSON string
     */
    public Review jsonToReview(String json){
        Gson gson = new Gson();
        Review review = gson.fromJson(json, Review.class);
        return review;
    }

}