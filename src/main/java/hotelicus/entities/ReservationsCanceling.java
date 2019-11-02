package hotelicus.entities;

import javax.persistence.*;

@Entity
@Table(name="reservations_canceling")
public class ReservationsCanceling {
    private Integer cancelingTypeId;
    private String reason;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="canceling_type_id")
    public Integer getCancelingTypeId() {
        return cancelingTypeId;
    }

    public void setCancelingTypeId(Integer cancelingTypeId) {
        this.cancelingTypeId = cancelingTypeId;
    }
    @Column(name="reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
