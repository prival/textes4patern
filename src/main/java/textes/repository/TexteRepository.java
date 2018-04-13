package textes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import textes.model.Texte;

import javax.transaction.Transactional;

@Repository
public interface TexteRepository extends JpaRepository<Texte, Long> {

    @Transactional
    @Modifying
    @Query("update Texte set ordre = ?1 where id = ?2")
    void updateTexte(int ordre, long id);
}