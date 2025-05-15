package eu.deschler.dapro.Carmodel;

import eu.deschler.dapro.CarType.CarTypeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Automodell")
public class CarModelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "Bezeichnung", nullable = false, length = 20)
    private String bezeichnung;

    @Size(max = 20)
    @Column(name = "Hersteller", length = 20)
    private String hersteller;

    @Column(name = "Autoart")
    private Integer autoart;

    @ManyToOne
    @JoinColumn(name = "Autoart", referencedColumnName = "ID", insertable = false, updatable = false)
    private CarTypeEntity carType;

    @Column(name = "Sitzplaetze")
    private Integer sitzplaetze;

    @Column(name = "kw")
    private Integer kw;

    @Size(max = 10)
    @Column(name = "Treibstoff", length = 10)
    private String treibstoff;

    @Column(name = "PreisTag")
    private Float preisTag;

    @Column(name = "PreisKM")
    private Float preisKM;

    @Column(name = "Achsen")
    private Integer achsen;

    @Column(name = "Ladevolumen")
    private Integer ladevolumen;

    @Column(name = "Zuladung")
    private Integer zuladung;

    @Size(max = 2)
    @Column(name = "Fuehrerschein", length = 2)
    private String fuehrerschein;

    public String getFuehrerschein() {
        return fuehrerschein;
    }

    public void setFuehrerschein(String fuehrerschein) {
        this.fuehrerschein = fuehrerschein;
    }

    public Integer getZuladung() {
        return zuladung;
    }

    public void setZuladung(Integer zuladung) {
        this.zuladung = zuladung;
    }

    public Integer getLadevolumen() {
        return ladevolumen;
    }

    public void setLadevolumen(Integer ladevolumen) {
        this.ladevolumen = ladevolumen;
    }

    public Integer getAchsen() {
        return achsen;
    }

    public void setAchsen(Integer achsen) {
        this.achsen = achsen;
    }

    public Float getPreisKM() {
        return preisKM;
    }

    public void setPreisKM(Float preisKM) {
        this.preisKM = preisKM;
    }

    public Float getPreisTag() {
        return preisTag;
    }

    public void setPreisTag(Float preisTag) {
        this.preisTag = preisTag;
    }

    public String getTreibstoff() {
        return treibstoff;
    }

    public void setTreibstoff(String treibstoff) {
        this.treibstoff = treibstoff;
    }

    public Integer getKw() {
        return kw;
    }

    public void setKw(Integer kw) {
        this.kw = kw;
    }

    public Integer getSitzplaetze() {
        return sitzplaetze;
    }

    public void setSitzplaetze(Integer sitzplaetze) {
        this.sitzplaetze = sitzplaetze;
    }

    public Integer getAutoart() {
        return autoart;
    }

    public void setAutoart(Integer autoart) {
        this.autoart = autoart;
    }

    public String getAutoartBezeichnung() {
        return carType != null ? carType.getArt() : "Unbekannt";
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
