package br.com.projeto.docinhos.repository;

import br.com.projeto.docinhos.model.Produto;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {}
