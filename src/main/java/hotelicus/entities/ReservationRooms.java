package hotelicus.entities;

import javax.persistence.*;

@Entity
@Table(name="reservations_rooms")
public class ReservationRooms {
    private Reservations reservation;
    private Rooms room;

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
    @JoinColumn(name = "room", referencedColumnName = "room_id")
    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }
}
