package textes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import textes.exception.ResourceNotFoundException;
import textes.model.Categorie;
import textes.repository.CategorieRepository;

import javax.validation.Valid;
import java.util.List;

@Service
public class CategorieService {


    @Autowired
    CategorieRepository categorieRepository;

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll(sortByOrdreAsc());
    }

    private Sort sortByOrdreAsc() {
        return new Sort(Sort.Direction.ASC, "ordre");
    }

    public Categorie getCategorieById(@PathVariable(value = "id") Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", id));
    }

    public Categorie createCategorie(@Valid @RequestBody Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public void deleteCategorie(@Valid @RequestBody long id) {
        categorieRepository.deleteById(id);
    }
}
