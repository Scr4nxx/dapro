package eu.deschler.dapro.Car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "Auto")
public class CarEntity {
    @Id
    @Size(max = 10)
    @Column(name = "Kennzeichen", nullable = false, length = 10)
    private String kennzeichen;
    @Column(name = "KMStand")
    private Integer kmStand;
    @Column(name = "TUVTermin")
    private LocalDate tuvTermin;
    @Column(name = "Kaufdatum")
    private LocalDate kaufdatum;
    @Column(name = "Modell")
    private Integer modell;

    public String getKennzeichen() {
        return kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public Integer getKmStand() {
        return kmStand;
    }

    public void setKmStand(Integer kmStand) {
        this.kmStand = kmStand;
    }

    public LocalDate getTuvTermin() {
        return tuvTermin;
    }

    public void setTuvTermin(LocalDate tuvTermin) {
        this.tuvTermin = tuvTermin;
    }

    public LocalDate getKaufdatum() {
        return kaufdatum;
    }

    public void setKaufdatum(LocalDate kaufdatum) {
        this.kaufdatum = kaufdatum;
    }

    public Integer getModell() {
        return modell;
    }

    public void setModell(Integer modell) {
        this.modell = modell;
    }

    @Override
    public String toString() {
        return "CarEntity{" +
                "kennzeichen='" + kennzeichen + "'" +
                ", kMStand=" + kmStand +
                ", tUVTermin=" + tuvTermin +
                ", kaufdatum=" + kaufdatum +
                ", modell=" + modell +
                '}';
    }
}
