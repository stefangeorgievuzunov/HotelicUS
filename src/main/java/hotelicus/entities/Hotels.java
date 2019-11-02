package hotelicus.entities;

import javax.persistence.*;
import java.util.Date;

public class Hotels {
    private Integer hotelId;
    private Integer userOwnerId;
    private Integer userManagerId;
    private String name;
    private Integer capacity;
    private Integer status;
    private Date  dateCreated;
    private Date removedOn;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hotel_id")
    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }


    @ManyToOne
    @JoinColumn(name="user_owner_id",referencedColumnName = "user_owner_id")
    public Integer getUserOwnerId() {
        return userOwnerId;
    }

    public void setUserOwnerId(Integer userOwnerId) {
        this.userOwnerId = userOwnerId;
    }

    @OneToMany
    @JoinColumn(name="user_manager_id",referencedColumnName = "user_manager_id")
    public Integer getUserManagerId() {
        return userManagerId;
    }

    public void setUserManagerId(Integer userManagerId) {
        this.userManagerId = userManagerId;
    }
    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name="capacity")
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    @Column(name="status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name="date_created")
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    @Column(name="removed_on")
    public Date getRemovedOn() {
        return removedOn;
    }

    public void setRemovedOn(Date removedOn) {
        this.removedOn = removedOn;
    }
}
