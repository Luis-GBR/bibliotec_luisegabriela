
package br.escola.bibliotec.repository;

import br.escola.bibliotec.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findAllByOrderByTituloAsc();

}

