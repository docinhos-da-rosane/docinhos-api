package br.com.projeto.docinhos.repository;

import br.com.projeto.docinhos.model.Categoria;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
  List<Categoria> findAllByOrderByNomeAsc();
}
