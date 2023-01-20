package springboot.hotele.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hotele.models.Rezerwacja;

public interface RezerwacjaRepo extends JpaRepository<Rezerwacja, Integer> {

    List<Rezerwacja> findAllByGoscEmail(String email);          //szukanie wszystkich rezerwacji gościa po emailu

    List<Rezerwacja> findAllByPokojId(Integer id);              //szukanie wszystkich rezerwacji po id pokoju
    
    List<Rezerwacja> findAllByGoscId(Integer id);               //szukanie wszystkich rezerwacji po id gościa

}
