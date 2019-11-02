package hotelicus.entities;

import javax.persistence.*;

@Entity
@Table(name="reservation_status")
public class ReservationStatus {
    private Integer reservationStatusId;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_status_id")
    public Integer getReservationStatusId() {
        return reservationStatusId;
    }

    public void setReservationStatusId(Integer reservationStatusId) {
        this.reservationStatusId = reservationStatusId;
    }
    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
