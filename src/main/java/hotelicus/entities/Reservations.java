package hotelicus.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="reservations")
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
    private Date createdOn;
    private Date reservedFrom;
    private Date reservedTo;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_id")
    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    @ManyToOne
    @JoinColumn(name="hotel_id",referencedColumnName = "hotel_id")
    public Hotels getHotel() {
        return hotel;
    }

    public void setHotel(Hotels hotel) {
        this.hotel = hotel;
    }

    @ManyToOne
    @JoinColumn(name="client_id",referencedColumnName = "client_id")
    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "user_id")
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Column(name="package")
    public Packages getReservationPackage() {
        return reservationPackage;
    }

    public void setReservationPackage(Packages reservationPackage) {
        this.reservationPackage = reservationPackage;
    }

    @Column(name="reservation_type")
    public ReservationTypes getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationTypes reservationType) {
        this.reservationType = reservationType;
    }

    @Column(name="payment_type")
    public PaymentTypes getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypes paymentType) {
        this.paymentType = paymentType;
    }

    @Column(name="reservation_canceling")
    public ReservationCanceling getCancelingType() {
        return cancelingType;
    }

    public void setCancelingType(ReservationCanceling cancelingType) {
        this.cancelingType = cancelingType;
    }

    @Column(name="reservation_status")
    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    @Column(name="paid_money")
    public Double getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(Double paidMoney) {
        this.paidMoney = paidMoney;
    }

    @Column(name="created_on")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name="reserved_from")
    public Date getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(Date reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    @Column(name="reserved_to")
    public Date getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(Date reservedTo) {
        this.reservedTo = reservedTo;
    }
}