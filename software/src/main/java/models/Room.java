package models;
/**
 *
 * @author Nick Giovanopoulos
 */
public class Room {

    private String name;
    private int roomId;
    private int capacity;

    private String image;
    private int pricePerHour;
    private String description;

    private String slogan;
    private String location;

    public Room(String name, int capacity, int pricePerHour, String description, String slogan, String location, String image) {
        this.name = name;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
        this.description = description;
        this.slogan = slogan;
        this.location = location;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}