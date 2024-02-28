package database.init;

import database.tables.*;
import models.*;
import static database.DB_Connection.getInitialConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Nick Giovanopoulos
 */
public class InitDatabase {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        InitDatabase init = new InitDatabase();
        init.initDatabase();
        init.initTables();
        init.addToDatabaseExamples();
    }

    /**
     Initializes the database by creating a new database with the name "HY351_PROJECT".
     @throws SQLException If an SQL error occurs while executing the database query.
     @throws ClassNotFoundException If the specified database driver class cannot be found.
     */
    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE HY351_PROJECT");
        stmt.close();
        conn.close();
    }

    /**
     Initializes the tables in the database by creating new tables for admins, customers, rooms,
     transactions, reservations, and reviews.
     @throws SQLException If an SQL error occurs while executing the database query.
     @throws ClassNotFoundException If the specified database driver class cannot be found.
     */
    public void initTables() throws SQLException, ClassNotFoundException {
        AdminTable admintable = new AdminTable();
        admintable.createAdminTable();

        CostumerTable costumertable = new CostumerTable();
        costumertable.createCostumerTable();

        RoomTable roomTable = new RoomTable();
        roomTable.createRoomTable();

        TransactionTable transactionTable = new TransactionTable();
        transactionTable.createTransactionTable();

        ReservationTable reservationtable = new ReservationTable();
        reservationtable.createReservationTable();

        ReviewTable reviewtable = new ReviewTable();
        reviewtable.createReviewTable();
    }

    /**
     * This function adds examples to the database for demonstration and testing purposes.
     * It creates instances of different database tables such as AdminTable, CostumerTable, ReservationTable,
     * ReviewTable, RoomTable, and TransactionTable and adds data to them.
     *
     * @throws ClassNotFoundException when the JDBC driver is not found
     * @throws SQLException when there is an error with SQL queries or operations
     * @throws IOException when there is an error with file operations
     */
    public void addToDatabaseExamples() throws ClassNotFoundException, SQLException, IOException {
        AdminTable admintable = new AdminTable();
        CostumerTable costumertable = new CostumerTable();
        ReservationTable reservationtable = new ReservationTable();
        ReviewTable reviewtable = new ReviewTable();
        RoomTable roomTable = new RoomTable();
        TransactionTable transactionTable = new TransactionTable();

        Admin admin = new Admin("Nick","Giovanopoulos","admin","admin","csd4613@csd.uoc.gr","Male","12/02/2002","6980111111");
        admintable.addAdmin(admin);
        //ADD   ITS PROFILE IMAGES
        admintable.addAdminProfileImg(1,"Nick","Giovanopoulos");
        // CREATE ROOMS
        Room room1 = new Room("Amphitheater Alpha",
                130,
                24,
                "Welcome to Amphitheater Alpha, where you can elevate your learning" +
                        " experience! With a capacity of 130, this room is perfect for large lectures" +
                        " or presentations. The room is equipped with state-of-the-art technology to enhance " +
                        "your teaching or learning experience. The rate for this room is $24/hour, so book now " +
                        "and take your education to new heights!",
                "Elevate Your Learning in Alpha",
                "First floor",
                "c1.png");
        Room room2 = new Room("Amphitheater Beta",
                250,
                45,
                "Amphitheater Beta is perfect for those who want a more immersive learning experience. " +
                        "With a capacity of 250, this room is ideal for hosting large events such as conferences or" +
                        " symposiums. The room is equipped with top-of-the-line technology, including projection and" +
                        " sound systems, to help you deliver your message effectively. The rate for this room is " +
                        "$45/hour, so book now and elevate your educational experience to the next level!",
                "Experience Beta - Better Education",
                "First Floor",
                "c2.png");
        Room room3 = new Room("Amphitheater Gamma",
                180,
                33,
                "With a capacity of 180, this room is perfect for lectures, seminars, and workshops." +
                        " The room is equipped with everything you need to ensure a successful event, including" +
                        " Wi-Fi, projectors, and sound systems. The rate for this room is $33/hour, so book" +
                        " now and discover a new world of learning!",
                "Gamma - Where Knowledge Meets Inspiration",
                "First Floor",
                "c3.png");
        Room room4 = new Room("Workshop Room Delta",
                45,
                10,
                "Workshop Room Delta is the perfect space for group activities, brainstorming sessions, " +
                        "and hands-on learning. With a capacity of 45, this room provides a comfortable and intimate" +
                        " setting for collaborative work. The room is equipped with whiteboards, projectors, and other" +
                        " tools to enhance your productivity. The rate for this room is $10/hour, so book now and start" +
                        " collaborating!",
                "Collaborate and Learn in Delta",
                "Second floor",
                "c4.png");
        Room room5 = new Room("Workshop Room Epsilon",
                30,
                7,
                "Welcome to Workshop Room Epsilon, where innovation and creativity meet. With a capacity of " +
                        "30, this room is perfect for small group activities and hands-on learning. The room is " +
                        "equipped with whiteboards, projectors, and other tools to help you unleash your creativity. " +
                        "The rate for this room is $6.5/hour, so book now and start innovating!",
                "Epsilon - The Space for Innovation",
                "Second floor",
                "c5.png");
        Room room6 = new Room("Lecture Room Zeta",
                60,
                15,
                "Welcome to Lecture Room Zeta, where education is accessible to everyone. With a capacity of " +
                        "60, this room is perfect for lectures, seminars, and workshops. The room is equipped with " +
                        "everything you need to ensure a successful event, including Wi-Fi, projectors, and sound " +
                        "systems. The rate for this room is $15/hour, so book now and take the first step towards " +
                        "empowering yourself and others through education.",
                "Zeta - Empowering Education for All",
                "Second floor",
                "c6.png");
        //ADD ROOMS TO TABLE
        roomTable.addRoom(room1);
        roomTable.addRoom(room2);
        roomTable.addRoom(room3);
        roomTable.addRoom(room4);
        roomTable.addRoom(room5);
        roomTable.addRoom(room6);
        //CREATE COSTUMERS
        Costumer c1 = new Costumer("George","Papadopoulos","georgepap","georgepap","geopap@gmail.com","Male","25/04/1999","6977248932",100);
        Costumer c2 = new Costumer("Nikos","Kalantas","nkalantas","nkalantas","geopap@gmail.com","Male","22/06/2000","6973248332",100);
        Costumer c3 = new Costumer("John","Georgiadis","johngeo","johngeo","johngeo@hotmail.com","Male","02/09/1996","6913248232",100);
        Costumer c4 = new Costumer("Maria","Dimitriou","mardim","mardim","mardim@hotmail.com","Female","25/04/1989","6977848538",100);
        Costumer c5 = new Costumer("Katerina","Mitsopoulou","katerinam","katerinam","katerinam@gmail.com","Female","11/08/1999","6980798332",100);
        //ADD COSTUMERS TO TABLE
        costumertable.addCostumer(c1);
        costumertable.addCostumer(c2);
        costumertable.addCostumer(c3);
        costumertable.addCostumer(c4);
        costumertable.addCostumer(c5);
        //ADD IMAGES
        costumertable.addProfileImg(1,"George","Papadopoulos");
        costumertable.addProfileImg(2,"Nikos","Kalantas");
        costumertable.addProfileImg(3,"John","Georgiadis");
        costumertable.addProfileImg(4,"Maria","Dimitriou");
        costumertable.addProfileImg(5,"Katerina","Mitsopoulou");
        //ADD IMAGES
        //CREATE TRANSACTIONS
        Transaction t1 = new Transaction("01/01/2023",45,"Wallet");
        Transaction t2 = new Transaction("02/01/2023",30,"InPerson");
        Transaction t3 = new Transaction("03/01/2023",60,"InPerson");
        Transaction t4 = new Transaction("04/01/2023",45,"Wallet");
        //ADD TRANSACTIONS
        transactionTable.addTransaction(t1);
        transactionTable.addTransaction(t2);
        transactionTable.addTransaction(t3);
        transactionTable.addTransaction(t4);
        //CREATE RESERVATIONS
        Reservation res1 = new Reservation(1,1,1,"01/02/2023",1,"COMPLETED");
        Reservation res2 = new Reservation(1,2,2,"01/02/2023",4,"COMPLETED");
        Reservation res3 = new Reservation(2,3,3,"01/02/2023",1,"COMPLETED");
        Reservation res4 = new Reservation(2,4,4,"01/02/2023",1,"COMPLETED");
        reservationtable.addReservation(res1);
        reservationtable.addReservation(res2);
        reservationtable.addReservation(res3);
        reservationtable.addReservation(res4);
        //CREATE REVIEWS
        Review r1 = new Review("The overall experience was great.",4,1);
        Review r2 = new Review("Everything was exactly as i expected!",5,2);
        Review r3 = new Review("Great but the price is a bit high",3,3);
        Review r4 = new Review("I am not satisfied at all by the equipment of the room",2,4);
        // ADD REVIEWS
        reviewtable.addReview(r1);
        reviewtable.addReview(r2);
        reviewtable.addReview(r3);
        reviewtable.addReview(r4);
    }
}
