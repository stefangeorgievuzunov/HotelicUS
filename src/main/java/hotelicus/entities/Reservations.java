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
    private ReservationsCanceling cancelingType;
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

    @ManyToOne
    @JoinColumn(name="package_id",referencedColumnName = "package_id")
    public Packages getPackage() {
        return reservationPackage;
    }

    public void setPackage(Packages reservationPackage) {
        this.reservationPackage = reservationPackage;
    }
    //TODO
    @ManyToMany
   // @JoinColumn(name="reservation_type_id",referencedColumnName = "reservation_type_id")
    public ReservationTypes getReservationType() {
        return reservationType;
    }

//    public void setReservationType(Integer reservationTypeId) {
//        this.reservationType = reservationTypeId;
//    }
    //TODO
    @ManyToMany
    //@JoinColumn(name="payment_type_id",referencedColumnName = "payment_type_id")
    public PaymentTypes getPaymentTypeId() {
        return this.paymentType;
    }

//    public void setPaymentTypeId(Integer paymentTypeId) {
//        this.paymentType = paymentType;
//    }
    //TODO
    @ManyToMany
    //@JoinColumn(name="canceling_type_id",referencedColumnName = "canceling_type_id")
    public ReservationsCanceling getCancelingTypeId() {
        return this.cancelingType;
    }

//    public void setCancelingTypeId(Integer cancelingTypeId) {
//        this.cancelingTypeId = cancelingTypeId;
//    }

    @Column(name="paid_money")
    public Double getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(Double paidMoney) {
        this.paidMoney = paidMoney;
    }
    //TODO
    @ManyToMany
    //@JoinColumn(name="reservation_status_id",referencedColumnName = "reservation_status_id")
    public ReservationStatus getReservationStatusId() {
        return reservationStatus;
    }

//    public void setReservationStatusId(Integer reservationStatusId) {
//        this.reservationStatus = reservationStatusId;
//    }

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
