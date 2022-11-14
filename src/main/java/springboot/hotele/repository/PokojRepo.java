package springboot.hotele.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hotele.models.Pokoj;

public interface PokojRepo extends JpaRepository<Pokoj, Integer> {
    
}
