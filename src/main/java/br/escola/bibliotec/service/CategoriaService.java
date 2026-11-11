
package br.escola.bibliotec.service;

import br.escola.bibliotec.model.Categoria;
import br.escola.bibliotec.repository.CategoriaRepository;
import br.escola.bibliotec.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final LivroRepository livroRepository;

    public CategoriaService(
            CategoriaRepository categoriaRepository,
            LivroRepository livroRepository) {
        this.categoriaRepository = categoriaRepository;
        this.livroRepository = livroRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria salvar(Categoria categoria) {
        if (categoria.getNome() != null) {
            categoria.setNome(categoria.getNome().trim());
        }
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public void excluir(Long id) {
        categoriaRepository.deleteById(id);
    }

    public boolean nomeJaExiste(Categoria categoria) {
        if (categoria.getNome() == null) {
            return false;
        }

        return categoriaRepository.findByNomeIgnoreCase(categoria.getNome().trim())
                .filter(existente -> !Objects.equals(
                        existente.getId(), categoria.getId()))
                .isPresent();
    }

    public boolean possuiLivros(Long id) {
        return livroRepository.existsByCategoriaId(id);
    }
}
