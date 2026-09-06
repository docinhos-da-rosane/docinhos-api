package br.com.projeto.docinhos.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_produtos")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(nullable = false, length = 500)
  private String descricao;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_categoria", nullable = false)
  private Categoria categoria;

  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProdutoPorcao> porcoes = new ArrayList<>();

  @Column(nullable = false)
  private boolean disponivel = true;

  @Setter(AccessLevel.NONE)
  @Column(name = "data_criacao", nullable = false, updatable = false)
  private Instant dataCriacao;

  @Setter(AccessLevel.NONE)
  @Column(name = "data_atualizacao", nullable = false)
  private Instant dataAtualizacao;

  public Produto(String nome, String descricao, Categoria categoria) {
    this.nome = nome;
    this.descricao = descricao;
    this.categoria = categoria;
  }

  @PrePersist
  private void prePersist() {
    Instant agora = Instant.now();

    dataCriacao = agora;
    dataAtualizacao = agora;
  }

  @PreUpdate
  private void preUpdate() {
    dataAtualizacao = Instant.now();
  }

  public void adicionarPorcoes(List<ProdutoPorcao> porcoes) {
    for (ProdutoPorcao porcao : porcoes) {
      adicionarPorcao(porcao);
    }
  }

  public void removerPorcoes(List<ProdutoPorcao> porcoes) {
    for (ProdutoPorcao porcao : porcoes) {
      removerPorcao(porcao);
    }
  }

  private void adicionarPorcao(ProdutoPorcao porcao) {
    porcoes.add(porcao);
    porcao.definirProduto(this);
  }

  private void removerPorcao(ProdutoPorcao porcao) {
    porcoes.remove(porcao);
    porcao.definirProduto(null);
  }
}
