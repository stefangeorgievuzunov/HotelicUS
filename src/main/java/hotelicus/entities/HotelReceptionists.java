package hotelicus.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="hotel_receptionists")
public class HotelReceptionists implements Serializable {
    private Hotels hotel;
    private Users receptionist;

    @Id
    @OneToOne
    @JoinColumn(name = "hotel", referencedColumnName = "hotel_id")
    public Hotels getHotel() {
        return hotel;
    }

    public void setHotel(Hotels hotel) {
        this.hotel = hotel;
    }
    @Id
    @OneToOne
    @JoinColumn(name = "receptionist", referencedColumnName = "user_id")
    public Users getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Users receptionist) {
        this.receptionist = receptionist;
    }
}
