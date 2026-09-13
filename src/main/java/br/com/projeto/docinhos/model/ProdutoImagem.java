package br.com.projeto.docinhos.model;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_produtos_imagens")
@Getter
@Setter
@NoArgsConstructor
public class ProdutoImagem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 500)
  private String url;

  @Column(nullable = false, length = 255)
  private String idArmazenamento;

  @Setter(AccessLevel.NONE)
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_produto", nullable = false, unique = true)
  private Produto produto;

  public ProdutoImagem(String url, String idArmazenamento) {
    this.url = url;
    this.idArmazenamento = idArmazenamento;
  }

  public void definirProduto(Produto produto) {
    this.produto = produto;
  }
}
