package hotelicus.entities;

import javax.persistence.*;

@Embeddable
@Table(name="packages_services")
public class PackagesServices {
    private Packages rPackage;
    private Services service;
    //TODO
    @ManyToMany
   // @JoinColumn(name="package_id",referencedColumnName = "package_id")
    public Packages getPackage_id() {
        return rPackage;
    }

//    public void setPackage_id(Integer package_id) {
//        this.package_id = package_id;
//    }
    //TODO
    @ManyToMany
    //@JoinColumn(name="service_id",referencedColumnName = "service_id")
    public Services getService_id() {
        return service;
    }

//    public void setService_id(Integer service_id) {
//        this.service_id = service_id;
//    }
}
