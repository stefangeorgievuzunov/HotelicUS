package hotelicus.entities;

import javax.persistence.*;

@Entity
@Table(name="reservation_types")
public class ReservationTypes {
    private Integer reservationTypeId;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_type_id")
    public Integer getReservationTypeId() {
        return reservationTypeId;
    }

    public void setReservationTypeId(Integer reservationTypeId) {
        this.reservationTypeId = reservationTypeId;
    }
    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
