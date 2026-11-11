
package br.escola.bibliotec.service;

import br.escola.bibliotec.model.Livro;
import br.escola.bibliotec.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAllByOrderByTituloAsc();
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

