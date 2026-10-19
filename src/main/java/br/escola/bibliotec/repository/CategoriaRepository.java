package br.escola.bibliotec.repository;

import br.escola.bibliotec.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}