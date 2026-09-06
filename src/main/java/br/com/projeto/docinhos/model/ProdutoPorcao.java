package br.com.projeto.docinhos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_produtos_porcoes")
@Getter
@Setter
public class ProdutoPorcao {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private int quantidade;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal preco;

  @Setter(AccessLevel.NONE)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_produto", nullable = false)
  private Produto produto;

  public ProdutoPorcao(int quantidade, BigDecimal preco) {
    this.quantidade = quantidade;
    this.preco = preco;
  }

  public void definirProduto(Produto produto) {
    this.produto = produto;
  }
}
