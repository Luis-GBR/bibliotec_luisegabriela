
package br.escola.bibliotec.service;

import br.escola.bibliotec.model.Livro;
import br.escola.bibliotec.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAllByOrderByTituloAsc();
    }

    public List<Livro> buscar(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return listarTodos();
        }

        return livroRepository.findByTituloContainingIgnoreCaseOrderByTituloAsc(titulo.trim());
    }
    public boolean isbnJaExiste(Livro livro) {
        Optional<Livro> encontrado = livroRepository.findByIsbn(livro.getIsbn());

        return encontrado.isPresent()
                && !encontrado.get().getId().equals(livro.getId());
    }
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    public void excluir(Long id) {
        livroRepository.deleteById(id);
    }

    public void salvar(Livro livro) {
        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());

        if (livro.getId() == null) {
            livro.setQuantidadeDisponivel(livro.getQuantidadeTotal());
        } else {
            Livro atual = livroRepository.findById(livro.getId()).orElseThrow();
            int emprestados = atual.getQuantidadeTotal() - atual.getQuantidadeDisponivel();
            livro.setQuantidadeDisponivel(livro.getQuantidadeTotal() - emprestados);
        }

        livroRepository.save(livro);
    }
}
