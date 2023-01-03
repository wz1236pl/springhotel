package springboot.hotele.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hotele.models.Gosc;

public interface GoscRepo extends JpaRepository<Gosc, Integer> {
    
    Gosc findByIdIs(Integer id);

    Gosc findByDokumentIs(String a);

    Gosc findByEmail(String email);

}
