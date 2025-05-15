package eu.deschler.dapro.CarType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Autoarten")
public class CarTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "Art", nullable = false, length = 20)
    private String art;

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
