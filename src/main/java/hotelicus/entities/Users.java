package hotelicus.entities;

import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="users")
public class Users {
    private Integer userId;
    private String username;
    private String password;
    private UserPrivileges privileges;
    private String firstName;
    private String lastName;
    private UserState userState;
    private Date startedOn;
    private Date endedOn;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Column(name="password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="privileges")
    public UserPrivileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(UserPrivileges privileges) {
        this.privileges = privileges;
    }

    @Column(name="first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name="last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="user_state")
    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    @Column(name="started_on")
    public Date getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    @Column(name="ended_on")
    public Date getEndedOn() {
        return endedOn;
    }

    public void setEndedOn(Date endedOn) {
        this.endedOn = endedOn;
    }
}
