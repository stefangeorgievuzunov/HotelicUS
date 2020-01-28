package hotelicus.entities;

import hotelicus.enums.HotelState;
import org.hibernate.mapping.Join;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotels")
public class Hotels {
    private Integer hotelId;
    private Users owner;
    private Users manager;
    private String name;
    private HotelState hotelState;
    private LocalDate createdOn;
    private LocalDate removedOn;


    public Hotels() {
        this.setCreatedOn(LocalDate.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    @OneToOne
    @JoinColumn(name = "user_owner_id", referencedColumnName = "user_id")
    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    @OneToOne
    @JoinColumn(name = "user_manager_id", referencedColumnName = "user_id")
    public Users getManager() {
        return manager;
    }

    public void setManager(Users manager) {
        this.manager = manager;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public HotelState getHotelState() {
        return hotelState;
    }

    public void setHotelState(HotelState hotelState) {
        this.hotelState = hotelState;
    }

    @Column(name = "created_on")
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "removed_on")
    public LocalDate getRemovedOn() {
        return removedOn;
    }

    public void setRemovedOn(LocalDate removedOn) {
        this.removedOn = removedOn;
    }
}
