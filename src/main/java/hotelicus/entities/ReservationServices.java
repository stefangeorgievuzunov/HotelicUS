package hotelicus.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="reservation_services")
public class ReservationServices implements Serializable {
    private Reservations reservation;
    private Services service;

    @Id
    @OneToOne
    @JoinColumn(name = "reservation", referencedColumnName = "reservation_id")
    public Reservations getReservation() {
        return reservation;
    }

    public void setReservation(Reservations reservation) {
        this.reservation = reservation;
    }

    @Id
    @OneToOne
    @JoinColumn(name = "service", referencedColumnName = "service_id")
    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }
}
