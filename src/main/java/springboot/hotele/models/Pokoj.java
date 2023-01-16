package springboot.hotele.models;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="pokoj")
public class Pokoj{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private int nrPokoju;
    private int miejsca;
    private String opis;
    private Float cena;
    @OneToMany(mappedBy = "pokoj")
    private List<Rezerwacja> rezerwacja  = new ArrayList<Rezerwacja>();

    public Pokoj(){}

    public Pokoj( int nrPokoju, int miejsca, String opis, Float cena) {
        this.nrPokoju = nrPokoju;
        this.miejsca = miejsca;
        this.opis = opis;
        this.cena = cena;
    }

    public void setNrPokoju(int nrPokoju) {
        this.nrPokoju = nrPokoju;
    }

    public void setMiejsca(int miejsca) {
        this.miejsca = miejsca;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }

    public int getNrPokoju() {
        return nrPokoju;
    }

    public int getMiejsca() {
        return miejsca;
    }

    public String getOpis() {
        return opis;
    }

    public Float getCena() {
        return cena;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Rezerwacja> getRezerwacja() {
        return rezerwacja;
    }

    public void setRezerwacja(List<Rezerwacja> rezerwacja) {
        this.rezerwacja = rezerwacja;
    }

    @Override
    public String toString() {
        return "Pokoj [id=" + id + ", nrPokoju=" + nrPokoju + ", miejsca=" + miejsca + ", opis=" + opis + ", cena="
                + cena+"]";
    }
    
}
