package textes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import textes.model.EtudeEnCours;

@Repository
public interface EtudeEnCoursRepository extends JpaRepository<EtudeEnCours, Long> {


}
