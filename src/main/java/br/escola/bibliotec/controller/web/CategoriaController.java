
package br.escola.bibliotec.controller.web;

import br.escola.bibliotec.model.Categoria;
import br.escola.bibliotec.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Categoria> categorias = categoriaService.listarTodas();
        model.addAttribute("categorias", categorias);
        return "categorias/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";
    }

    @PostMapping
    public String salvar(
            @Valid @ModelAttribute("categoria") Categoria categoria,
            BindingResult resultado,
            RedirectAttributes redirectAttributes) {

        if (categoria.getNome() != null
                && !categoria.getNome().isBlank()
                && categoriaService.nomeJaExiste(categoria)) {
            resultado.rejectValue(
                    "nome",
                    "nome.duplicado",
                    "Já existe uma categoria com este nome"
            );
        }

        if (resultado.hasErrors()) {
            return "categorias/form";
        }

        categoriaService.salvar(categoria);
        redirectAttributes.addFlashAttribute(
                "mensagem", "Categoria salva com sucesso!"
        );

        return "redirect:/categorias";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        return categoriaService.buscarPorId(id)
                .map(categoria -> {
                    model.addAttribute("categoria", categoria);
                    return "categorias/form";
                })
                .orElse("redirect:/categorias");
    }

    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        categoriaService.excluir(id);
        redirectAttributes.addFlashAttribute(
                "mensagem", "Categoria excluída."
        );

        return "redirect:/categorias";
    }
}

