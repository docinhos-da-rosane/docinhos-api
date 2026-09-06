package br.com.projeto.docinhos.mocks;

import br.com.projeto.docinhos.dto.request.CriarProdutoPorcaoRequest;
import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoPorcaoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.model.Categoria;
import br.com.projeto.docinhos.model.Produto;
import br.com.projeto.docinhos.model.ProdutoPorcao;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ProdutoMock {

  public static CriarProdutoRequest criarProdutoRequestPadrao() {
    return criarProdutoRequest().build();
  }

  public static CriarProdutoRequest.CriarProdutoRequestBuilder criarProdutoRequest() {
    return CriarProdutoRequest.builder()
        .nome("Produto")
        .descricao("Descrição do Produto")
        .categoriaId(UUID.randomUUID())
        .porcoes(List.of(criarProdutoPorcaoRequest().build()));
  }

  public static CriarProdutoPorcaoRequest.CriarProdutoPorcaoRequestBuilder
      criarProdutoPorcaoRequest() {
    return CriarProdutoPorcaoRequest.builder().quantidade(10).preco(new BigDecimal("19.99"));
  }

  public static ProdutoResponse criarProdutoResponsePadrao() {
    return ProdutoResponse.builder()
        .id(UUID.randomUUID())
        .nome("Produto 1")
        .descricao("Descrição do Produto 1")
        .disponivel(true)
        .categoria(CategoriaMock.criarCategoriaResponsePadrao())
        .porcoes(List.of(criarProdutoPorcaoResponsePadrao()))
        .dataCriacao(Instant.now())
        .dataAtualizacao(Instant.now())
        .build();
  }

  public static Produto criarProdutoPadrao(Categoria categoria) {
    Produto produto = new Produto("Produto 1", "Descrição do Produto 1", categoria);
    produto.setId(UUID.randomUUID());
    produto.adicionarPorcoes(List.of(criarProdutoPorcaoPadrao()));
    return produto;
  }

  public static ProdutoPorcao criarProdutoPorcaoPadrao() {
    ProdutoPorcao produtoPorcao = new ProdutoPorcao(10, new BigDecimal("19.99"));
    produtoPorcao.setId(UUID.randomUUID());
    return produtoPorcao;
  }

  private static ProdutoPorcaoResponse criarProdutoPorcaoResponsePadrao() {
    return ProdutoPorcaoResponse.builder()
        .id(UUID.randomUUID())
        .quantidade(10)
        .preco(new BigDecimal("19.99"))
        .build();
  }
}
