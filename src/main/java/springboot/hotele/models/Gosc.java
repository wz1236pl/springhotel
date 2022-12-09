package springboot.hotele.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Gosc {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String email;
    private String imie;
    private String nazwisko;
    private String telefon;
    private String dokument;

    public Gosc(){};
    
    public Gosc(String imie, String nazwisko, String telefon, String dokument) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.telefon = telefon;
        this.dokument = dokument;
    }

    public Integer getId() {
        return id;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getDokument() {
        return dokument;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setDokument(String dokument) {
        this.dokument = dokument;
    }

    @Override
    public String toString() {
        return "gosc [id=" + id + ", imie=" + imie + ", nazwisko=" + nazwisko + ", telefon=" + telefon + ", dokument="
                + dokument + "]";
    }
    
}
