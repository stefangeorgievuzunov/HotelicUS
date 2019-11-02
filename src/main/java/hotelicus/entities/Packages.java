package hotelicus.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="packages")
public class Packages implements Serializable {
    private Integer packageId;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="package_id")
    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
