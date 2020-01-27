package hotelicus.entities;

import hotelicus.enums.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "reservations")
public class Reservations {
    private Integer reservationId;
    private Hotels hotel;
    private Clients client;
    private Users user;
    private Packages reservationPackage;
    private ReservationTypes reservationType;
    private PaymentTypes paymentType;
    private ReservationCanceling cancelingType;
    private Double paidMoney;
    private ReservationStatus reservationStatus;
    private LocalDate createdOn;
    private LocalDate reservedFrom;
    private LocalDate reservedTo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    @OneToOne
    @JoinColumn(name = "hotel_fk_id", referencedColumnName = "hotel_id")
    public Hotels getHotel() {
        return hotel;
    }

    public void setHotel(Hotels hotel) {
        this.hotel = hotel;
    }

    @OneToOne
    @JoinColumn(name = "client_fk_id", referencedColumnName = "client_id")
    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    @OneToOne
    @JoinColumn(name = "receptionist_id", referencedColumnName = "user_id")
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Column(name = "package")
    public Packages getReservationPackage() {
        return reservationPackage;
    }

    public void setReservationPackage(Packages reservationPackage) {
        this.reservationPackage = reservationPackage;
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_type")
    public ReservationTypes getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationTypes reservationType) {
        this.reservationType = reservationType;
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    public PaymentTypes getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypes paymentType) {
        this.paymentType = paymentType;
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "canceling_type")
    public ReservationCanceling getCancelingType() {
        return cancelingType;
    }

    public void setCancelingType(ReservationCanceling cancelingType) {
        this.cancelingType = cancelingType;
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status")
    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    @Column(name = "paid_money")
    public Double getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(Double paidMoney) {
        this.paidMoney = paidMoney;
    }

    @Column(name = "created_on")
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "reserved_from")
    public LocalDate getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(LocalDate reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    @Column(name = "reserved_to")
    public LocalDate getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(LocalDate reservedTo) {
        this.reservedTo = reservedTo;
    }
}
