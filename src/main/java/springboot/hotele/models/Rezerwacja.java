package springboot.hotele.models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="rezerwacja")
public class Rezerwacja {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private Date dataStart;
    private Date dataEnd;
    @ManyToOne
    private Pokoj pokoj;
    @ManyToOne
    private Gosc gosc;

    public Rezerwacja() {};

    public Rezerwacja(Date dataStart, Date dataEnd, Pokoj pokoj, Gosc gosc) {
        this.dataStart = dataStart;
        this.dataEnd = dataEnd;
        this.pokoj = pokoj;
        this.gosc = gosc;
    }

    public Integer getId() {
        return id;
    }

    public Date getDataStart() {
        return dataStart;
    }

    public Date getDataEnd() {
        return dataEnd;
    }
    
    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public void setDataEnd(Date dataEnd) {
        this.dataEnd = dataEnd;
    }

    public Pokoj getPokoj() {
        return pokoj;
    }

    public void setPokoj(Pokoj pokoj) {
        this.pokoj = pokoj;
    }

    public Gosc getGosc() {
        return gosc;
    }

    public void setGosc(Gosc gosc) {
        this.gosc = gosc;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Rezerwacja [id=" + id + ", dataStart=" + dataStart + ", dataEnd=" + dataEnd + ", pokoj=" + pokoj
                + ", gosc=" + gosc + "]";
    }

}
