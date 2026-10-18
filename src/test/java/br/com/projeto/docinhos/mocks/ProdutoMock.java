package br.com.projeto.docinhos.mocks;

import br.com.projeto.docinhos.dto.request.CriarProdutoPorcaoRequest;
import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoDetalhadoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoPorcaoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResumoResponse;
import br.com.projeto.docinhos.model.Categoria;
import br.com.projeto.docinhos.model.Produto;
import br.com.projeto.docinhos.model.ProdutoImagem;
import br.com.projeto.docinhos.model.ProdutoPorcao;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

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

  public static ProdutoResumoResponse criarProdutoResumoResponsePadrao() {
    return ProdutoResumoResponse.builder()
        .id(UUID.randomUUID())
        .nome("Brigadeiro")
        .categoria("Tradicional")
        .imagemMiniaturaUrl("https://res.cloudinary.com/demo/image/upload/c_lfill/produto.jpg")
        .disponivel(true)
        .build();
  }

  public static ProdutoResponse criarProdutoResponsePadrao() {
    return ProdutoResponse.builder()
        .id(UUID.randomUUID())
        .nome("Produto 1")
        .descricao("Descrição do Produto 1")
        .imagemUrl("https://example.com/imagem.jpg")
        .disponivel(true)
        .categoria(CategoriaMock.criarCategoriaResponsePadrao())
        .porcoes(List.of(criarProdutoPorcaoResponsePadrao()))
        .dataCriacao(Instant.now())
        .dataAtualizacao(Instant.now())
        .build();
  }

  public static ProdutoDetalhadoResponse criarProdutoDetalhadoResponsePadrao() {
    return ProdutoDetalhadoResponse.builder()
        .id(UUID.randomUUID())
        .nome("Brigadeiro")
        .descricao("Descrição completa")
        .categoria("Tradicionais")
        .disponivel(true)
        .imagemUrl("https://example.com/imagem.jpg")
        .porcoes(List.of(criarProdutoPorcaoResponsePadrao()))
        .build();
  }

  public static Produto criarProdutoPadrao(Categoria categoria) {
    Produto produto = new Produto("Produto 1", "Descrição do Produto 1", categoria);
    produto.setId(UUID.randomUUID());
    produto.adicionarPorcoes(List.of(criarProdutoPorcaoPadrao()));
    produto.adicionarImagem(criarProdutoImagemPadrao());
    return produto;
  }

  public static ProdutoPorcao criarProdutoPorcaoPadrao() {
    return criarProdutoPorcao(10, "19.99");
  }

  public static ProdutoPorcao criarProdutoPorcao(int quantidade, String preco) {
    ProdutoPorcao porcao = new ProdutoPorcao(quantidade, new BigDecimal(preco));
    porcao.setId(UUID.randomUUID());
    return porcao;
  }

  public static ProdutoImagem criarProdutoImagemPadrao() {
    ProdutoImagem produtoImagem =
        new ProdutoImagem("https://example.com/imagem.jpg", "idArmazenamento123");
    produtoImagem.setId(UUID.randomUUID());
    return produtoImagem;
  }

  public static MockMultipartFile criaMultiPartImagemPadrao() {
    return new MockMultipartFile(
        "imagem",
        "produto.png",
        MediaType.IMAGE_PNG_VALUE,
        "imagem".getBytes(StandardCharsets.UTF_8));
  }

  private static ProdutoPorcaoResponse criarProdutoPorcaoResponsePadrao() {
    return ProdutoPorcaoResponse.builder()
        .id(UUID.randomUUID())
        .quantidade(10)
        .preco(new BigDecimal("19.99"))
        .build();
  }
}
