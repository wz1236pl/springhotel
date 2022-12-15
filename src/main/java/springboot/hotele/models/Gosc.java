package springboot.hotele.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="gosc") 
public class Gosc {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    private String imie;
    private String nazwisko;
    private String telefon;
    private String dokument;
    @OneToMany(mappedBy = "gosc")  
    private List<Rezerwacja> rezerwacja  = new ArrayList<Rezerwacja>(); 


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Gosc [id=" + id + ", email=" + email + ", password=" + password + ", role=" + role + ", imie=" + imie
                + ", nazwisko=" + nazwisko + ", telefon=" + telefon + ", dokument=" + dokument + ", rezerwacja="
                + rezerwacja + "]";
    }

    

}
