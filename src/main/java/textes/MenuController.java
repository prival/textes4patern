package textes;

import org.aspectj.weaver.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import textes.model.Categorie;
import textes.service.CategorieService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MenuController {

    @Autowired
    CategorieService categorieService;

    @RequestMapping("/")
    public String index() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(HttpSession session) {
        session.setAttribute("categoriesMenu", categorieService.getAllCategories());
        return "index";
    }

}