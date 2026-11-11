
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String listar(@RequestParam(required = false) String titulo, Model model) {
        model.addAttribute("livros", livroService.buscar(titulo));
        model.addAttribute("titulo", titulo);
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Livro livro = new Livro();
        livro.setCategoria(new Categoria());
        model.addAttribute("livro", livro);
        return formulario(model);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model,
                         RedirectAttributes redirectAttributes) {
        var livro = livroService.buscarPorId(id);

        if (livro.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagem", "Livro não encontrado!");
            return "redirect:/livros";
        }

        model.addAttribute("livro", livro.get());
        return formulario(model);
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        livroService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Livro excluído com sucesso!");
        return "redirect:/livros";
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

        if (livroService.isbnJaExiste(livro)) {
            resultado.rejectValue("isbn", "isbn.duplicado",
                    "Já existe um livro com este ISBN");
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
