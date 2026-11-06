package br.escola.bibliotec.service;

import br.escola.bibliotec.model.Categoria;
import br.escola.bibliotec.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
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
                .filter(existente -> !Objects.equals(existente.getId(), categoria.getId()))
                .isPresent();
    }
}