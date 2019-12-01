package hotelicus.entities;
import javax.persistence.*;

@Entity
@Table(name = "loggedusers")
public class LoggedUsers {
    private Integer id;
    private Users loggedUser;


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
}
