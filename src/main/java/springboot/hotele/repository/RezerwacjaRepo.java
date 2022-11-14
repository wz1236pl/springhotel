package springboot.hotele.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.hotele.models.Rezerwacja;

public interface RezerwacjaRepo extends JpaRepository<Rezerwacja, Integer> {

   
}
