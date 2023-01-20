package springboot.hotele.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.hotele.models.Gosc;

public interface GoscRepo extends JpaRepository<Gosc, Integer> {
    
    Gosc findByIdIs(Integer id);    //szukamy gościa po id

    Gosc findByDokumentIs(String a);    //szukamy gościa po dokumencie

    Gosc findByEmail(String email); //szukamy gościa po emailu

}
