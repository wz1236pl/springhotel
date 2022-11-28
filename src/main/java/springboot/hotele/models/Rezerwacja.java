package springboot.hotele.models;



import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rezerwacja {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private Date dataStart;
    private Date dataEnd;
    private int idPokoj;
    private int idGosc;

    public Rezerwacja() {};

    public Rezerwacja(Date dataStart, Date dataEnd, int idPokoj, int idGosc) {
        this.dataStart = dataStart;
        this.dataEnd = dataEnd;
        this.idPokoj = idPokoj;
        this.idGosc = idGosc;
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

    public int getIdPokoj() {
        return idPokoj;
    }

    public int getIdGosc() {
        return idGosc;
    }

    
    
    public void setDataStart(Date dataStart) {
        this.dataStart = dataStart;
    }

    public void setDataEnd(Date dataEnd) {
        this.dataEnd = dataEnd;
    }

    public void setIdPokoj(int idPokoj) {
        this.idPokoj = idPokoj;
    }

    public void setIdGosc(int idGosc) {
        this.idGosc = idGosc;
    }

    @Override
    public String toString() {
        return "rezerwacja [id=" + id + ", dataStart=" + dataStart + ", dataEnd=" + dataEnd + ", idPokoj=" + idPokoj
                + "]";
    }

}
