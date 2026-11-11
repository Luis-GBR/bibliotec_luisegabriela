
package br.escola.bibliotec.controller.web;

import br.escola.bibliotec.model.Categoria;
import br.escola.bibliotec.model.Livro;
import br.escola.bibliotec.service.CategoriaService;
import br.escola.bibliotec.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;
    private final CategoriaService categoriaService;

    public LivroController(LivroService livroService, CategoriaService categoriaService) {
        this.livroService = livroService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("livros", livroService.listarTodos());
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Livro livro = new Livro();
        livro.setCategoria(new Categoria());
        model.addAttribute("livro", livro);
        return formulario(model);
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("livro") Livro livro,
                         BindingResult resultado,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (livro.getCategoria() == null || livro.getCategoria().getId() == null) {
            resultado.rejectValue("categoria.id", "categoria.obrigatoria",
                    "Escolha uma categoria");
        }

        if (resultado.hasErrors()) {
            return formulario(model);
        }

        livroService.salvar(livro);
        redirectAttributes.addFlashAttribute("mensagem", "Livro salvo com sucesso!");
        return "redirect:/livros";
    }

    private String formulario(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "livros/form";
    }
}

