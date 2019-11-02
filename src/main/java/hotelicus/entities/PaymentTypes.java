package hotelicus.entities;

import javax.persistence.*;

@Entity
@Table(name="payment_types")
public class PaymentTypes {
    private Integer paymentTypeId;
    private String name;

    @Id
    @Column(name="payment_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
