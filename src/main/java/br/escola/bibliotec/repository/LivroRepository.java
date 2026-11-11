
package br.escola.bibliotec.repository;

import br.escola.bibliotec.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findAllByOrderByTituloAsc();

    List<Livro> findByTituloContainingIgnoreCaseOrderByTituloAsc(String titulo);

    Optional<Livro> findByIsbn(String isbn);

    boolean existsByCategoriaId(Long categoriaId);

}
