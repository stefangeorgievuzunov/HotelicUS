package hotelicus.entities;

import hotelicus.enums.RoomCategories;
import hotelicus.enums.RoomStatus;

import javax.persistence.*;

@Entity
@Table(name = "rooms")
public class Rooms {
    private Integer roomId;
    private Hotels hotel;
    private RoomCategories category;
    private Integer capacity;
    private String roomNumber;
    private Double price;
    private RoomStatus status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    @OneToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id")
    public Hotels getHotel() {
        return hotel;
    }

    public void setHotel(Hotels hotel) {
        this.hotel = hotel;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    public RoomCategories getCategory() {
        return category;
    }

    public void setCategory(RoomCategories category) {
        this.category = category;
    }

    @Column(name = "capacity")
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Column(name = "room_number")
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String room_number) {
        this.roomNumber = room_number;
    }

    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status")
    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
