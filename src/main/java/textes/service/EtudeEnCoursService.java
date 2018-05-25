package textes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import textes.model.EtudeEnCours;
import textes.repository.EtudeEnCoursRepository;

import javax.validation.Valid;
import java.util.List;

@Service
public class EtudeEnCoursService {

    @Autowired
    EtudeEnCoursRepository etudeEnCoursRepository;

    public List<EtudeEnCours> getAllEtudesEnCours() {
        return etudeEnCoursRepository.findAll();
    }

    public EtudeEnCours createEtudeEnCours(@Valid @RequestBody EtudeEnCours etudeEnCours) {
        return etudeEnCoursRepository.save(etudeEnCours);
    }

    //    public void deleteEtudeEnCours(@Valid @RequestBody EtudeEnCours etudeEnCours) {
    public void deleteEtudeEnCours(@Valid @RequestBody long id) {
//        etudeEnCoursRepository.delete(etudeEnCours);
        etudeEnCoursRepository.deleteById(id);
    }
}
