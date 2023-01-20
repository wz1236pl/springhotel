package springboot.hotele;

import java.sql.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Sesja {                        
    private Date start;                 // klasa w której przechowujemy dane dotyczące wybranych dat
    private Date end;                   // przechowowana w pamięci httpsession
    
    public Sesja() {
    }

    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }
    @Override
    public String toString() {
        return "Sesja [start=" + start + ", end=" + end + "]";
    }
}
