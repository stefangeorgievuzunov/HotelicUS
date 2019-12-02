package hotelicus.entities;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "loggedusers")
public class LoggedUsers {
    private Integer id;
    private Users loggedUser;
    private Date lastPinged;

    public LoggedUsers(){
        this.setLastPinged(new Date());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "logged_user_id", referencedColumnName = "user_id")
    public Users getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Users loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Column(name="last_pinged")
    public Date getLastPinged() {
        return lastPinged;
    }

    public void setLastPinged(Date lastPinged) {
        this.lastPinged = lastPinged;
    }
}
