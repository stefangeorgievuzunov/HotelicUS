package hotelicus.entities;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="clients")
public class Clients {
    private Integer id;
    private Integer userId;
    private String firstName;
    private String lastName;
}
