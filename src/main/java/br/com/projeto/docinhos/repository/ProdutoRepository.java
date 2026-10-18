package br.com.projeto.docinhos.repository;

import br.com.projeto.docinhos.model.Produto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

  @EntityGraph(attributePaths = {"categoria", "imagem", "porcoes"})
  Optional<Produto> findProdutoById(UUID id);

  @Override
  @EntityGraph(attributePaths = {"categoria", "imagem"})
  Page<Produto> findAll(Pageable pageable);
}
