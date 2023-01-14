package springboot.hotele.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hotele.models.Rezerwacja;

public interface RezerwacjaRepo extends JpaRepository<Rezerwacja, Integer> {

    List<Rezerwacja> findAllByGoscEmail(String email);

    List<Rezerwacja> findAllByPokojId(Integer id);
    
    List<Rezerwacja> findAllByGoscId(Integer id);
}
