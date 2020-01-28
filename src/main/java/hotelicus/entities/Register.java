package hotelicus.entities;

import hotelicus.enums.ReservationStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="register")
public class Register {
    private Integer registerId;
    private LocalDate createdOn;
    private ReservationStatus status;
    private Reservations reservation;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    public Integer getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Integer registerId) {
        this.registerId = registerId;
    }

    @Column(name = "created_on")
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @OneToOne
    @JoinColumn(name = "reservation", referencedColumnName = "reservation_id")
    public Reservations getReservation() {
        return reservation;
    }

    public void setReservation(Reservations reservation) {
        this.reservation = reservation;
    }
}
