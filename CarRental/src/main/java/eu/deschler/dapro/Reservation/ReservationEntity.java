package eu.deschler.dapro.Reservation;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reservierung")
public class ReservationEntity {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "KundeID")
    private Integer kundeID;

    @Column(name = "ModellID")
    private Integer modellID;

    @Column(name = "Beginn")
    private LocalDateTime beginn;

    @Column(name = "Ende")
    private LocalDateTime ende;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKundeID() {
        return kundeID;
    }

    public void setKundeID(Integer kundeID) {
        this.kundeID = kundeID;
    }

    public Integer getModellID() {
        return modellID;
    }

    public void setModellID(Integer modellID) {
        this.modellID = modellID;
    }

    public LocalDateTime getBeginn() {
        return beginn;
    }

    public void setBeginn(LocalDateTime beginn) {
        this.beginn = beginn;
    }

    public LocalDateTime getEnde() {
        return ende;
    }

    public void setEnde(LocalDateTime ende) {
        this.ende = ende;
    }
}
