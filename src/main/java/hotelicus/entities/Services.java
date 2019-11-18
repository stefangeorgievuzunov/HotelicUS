package hotelicus.entities;

import javax.persistence.*;

@Entity
@Table(name="services")
public class Services {
    private Integer serviceId;
    private String  name;
    private Double price;

    public Services(){
        this.serviceId=0;
        this.name="";
        this.price=0.0;
    }
    public Services(Integer sId, String n, Double p) {
        this.serviceId = sId;
        this.name = n;
        this.price = p;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="service_id")
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
