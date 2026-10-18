package br.com.projeto.docinhos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.*;
import br.com.projeto.docinhos.exception.NaoEncontradoException;
import br.com.projeto.docinhos.mapper.CategoriaMapper;
import br.com.projeto.docinhos.mapper.PageMapper;
import br.com.projeto.docinhos.mapper.ProdutoMapper;
import br.com.projeto.docinhos.mapper.ProdutoPorcaoMapper;
import br.com.projeto.docinhos.mocks.CategoriaMock;
import br.com.projeto.docinhos.mocks.ProdutoMock;
import br.com.projeto.docinhos.model.Categoria;
import br.com.projeto.docinhos.model.Produto;
import br.com.projeto.docinhos.model.ProdutoImagem;
import br.com.projeto.docinhos.model.ProdutoPorcao;
import br.com.projeto.docinhos.repository.ProdutoRepository;
import br.com.projeto.docinhos.service.ProdutoImagemService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

  @Mock private ProdutoRepository produtoRepository;

  @Mock private CategoriaServiceImpl categoriaService;

  @Mock private ProdutoImagemService produtoImagemService;

  private CategoriaMapper categoriaMapper;
  private ProdutoPorcaoMapper produtoPorcaoMapper;
  private ProdutoMapper produtoMapper;
  private PageMapper pageResponseMapper;

  private ProdutoServiceImpl produtoService;

  @BeforeEach
  void setUp() {
    categoriaMapper = Mappers.getMapper(CategoriaMapper.class);
    produtoPorcaoMapper = Mappers.getMapper(ProdutoPorcaoMapper.class);
    produtoMapper = Mappers.getMapper(ProdutoMapper.class);
    pageResponseMapper = Mappers.getMapper(PageMapper.class);

    ReflectionTestUtils.setField(produtoMapper, "categoriaMapper", categoriaMapper);
    ReflectionTestUtils.setField(produtoMapper, "produtoPorcaoMapper", produtoPorcaoMapper);

    produtoService =
        new ProdutoServiceImpl(
            categoriaService,
            produtoImagemService,
            produtoRepository,
            produtoMapper,
            produtoPorcaoMapper,
            pageResponseMapper);
  }

  @Nested
  class CriarProdutoTest {

    @Test
    void deveCriarProdutoComSucesso() {
      CriarProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();
      MockMultipartFile imagem = ProdutoMock.criaMultiPartImagemPadrao();
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      ProdutoImagem produtoImagem = ProdutoMock.criarProdutoImagemPadrao();
      Produto produtoSalvo = ProdutoMock.criarProdutoPadrao(categoria);

      when(categoriaService.buscarCategoriaPorId(any())).thenReturn(categoria);
      when(produtoImagemService.armazenar(any())).thenReturn(produtoImagem);
      when(produtoRepository.save(any())).thenReturn(produtoSalvo);

      ProdutoResponse response = produtoService.criarProduto(imagem, request);

      assertEquals(produtoSalvo.getId(), response.id());
      assertEquals(produtoSalvo.getCategoria().getId(), response.categoria().id());
      assertEquals(produtoSalvo.getPorcoes().size(), response.porcoes().size());
      assertEquals(produtoSalvo.getImagem().getUrl(), response.imagemUrl());

      verify(produtoRepository).save(any());
      verify(produtoImagemService, never()).rollback(any());
    }

    @Test
    void deveLancarNaoEncontradoExceptionQuandoCategoriaNaoExistir() {
      CriarProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();
      MockMultipartFile imagem = ProdutoMock.criaMultiPartImagemPadrao();

      when(categoriaService.buscarCategoriaPorId(any()))
          .thenThrow(new NaoEncontradoException("Categoria não encontrada"));

      assertThrows(
          NaoEncontradoException.class,
          () -> {
            produtoService.criarProduto(imagem, request);
          });
    }

    @Test
    void deveFazerRollbackQuandoFalharSalvamentoDoProduto() {
      CriarProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();
      MockMultipartFile imagem = ProdutoMock.criaMultiPartImagemPadrao();
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      ProdutoImagem produtoImagem = ProdutoMock.criarProdutoImagemPadrao();

      when(categoriaService.buscarCategoriaPorId(any())).thenReturn(categoria);
      when(produtoImagemService.armazenar(any())).thenReturn(produtoImagem);
      when(produtoRepository.save(any())).thenThrow(new RuntimeException("Erro ao salvar produto"));

      assertThrows(
          RuntimeException.class,
          () -> {
            produtoService.criarProduto(imagem, request);
          });

      verify(produtoImagemService).rollback(produtoImagem);
    }
  }

  @Nested
  class ListarProdutosTests {

    @Test
    void deveListarProdutosComOrdenacaoPaginacaoETodosOsCampos() {
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      Produto brigadeiro = criarProduto("Brigadeiro", true, categoria);
      Produto beijinho = criarProduto("Beijinho", true, categoria);
      Produto alfajorIndisponivel = criarProduto("Alfajor", false, categoria);

      Pageable pageable = criarPaginacao(0, 6);

      Page<Produto> produtos =
          new PageImpl<>(List.of(beijinho, brigadeiro, alfajorIndisponivel), pageable, 7);

      when(produtoRepository.findAll(any(Pageable.class))).thenReturn(produtos);
      when(produtoImagemService.gerarUrlMiniatura(any(ProdutoImagem.class)))
          .thenReturn("https://example.com/miniatura.jpg");

      PageResponse<ProdutoResumoResponse> response = produtoService.listarProdutos(0, 6);

      assertEquals(
          List.of("Beijinho", "Brigadeiro", "Alfajor"),
          response.content().stream().map(ProdutoResumoResponse::nome).toList());
      assertEquals(categoria.getNome(), response.content().getFirst().categoria());
      assertEquals(
          "https://example.com/miniatura.jpg", response.content().getFirst().imagemMiniaturaUrl());
      assertEquals(0, response.page());
      assertEquals(6, response.size());
      assertEquals(7, response.totalElements());
      assertEquals(2, response.totalPages());

      assertEquals(beijinho.getNome(), response.content().getFirst().nome());
      assertEquals(alfajorIndisponivel.getNome(), response.content().getLast().nome());
      assertTrue(response.content().getFirst().disponivel());
      assertFalse(response.content().getLast().disponivel());

      verify(produtoRepository).findAll(any(Pageable.class));
      verify(produtoImagemService, times(3)).gerarUrlMiniatura(any(ProdutoImagem.class));
    }

    @Test
    void deveRetornarPaginaVaziaComTotaisZerados() {
      Pageable pageable = criarPaginacao(0, 6);
      when(produtoRepository.findAll(any(Pageable.class)))
          .thenReturn(new PageImpl<>(List.of(), pageable, 0));

      PageResponse<ProdutoResumoResponse> response = produtoService.listarProdutos(0, 6);

      assertTrue(response.content().isEmpty());
      assertEquals(0, response.totalElements());
      assertEquals(0, response.totalPages());
      verify(produtoImagemService, never()).gerarUrlMiniatura(any(ProdutoImagem.class));
    }
  }

  @Nested
  class BuscarProdutoTests {

    @Test
    void deveRetornarProdutoComPorcoesOrdenadasEImagemMedia() {
      ProdutoPorcao porcao50 = ProdutoMock.criarProdutoPorcao(50, "155.00");
      ProdutoPorcao porcao15 = ProdutoMock.criarProdutoPorcao(15, "55.00");

      Produto produto = criarProduto(List.of(porcao50, porcao15));
      String imagemUrl = "https://example.com/1200.jpg";

      when(produtoRepository.findProdutoById(produto.getId())).thenReturn(Optional.of(produto));
      when(produtoImagemService.gerarUrlMedia(produto.getImagem())).thenReturn(imagemUrl);

      ProdutoDetalhadoResponse response = produtoService.buscarProduto(produto.getId());

      assertEquals(imagemUrl, response.imagemUrl());
      assertEquals(produto.getCategoria().getNome(), response.categoria());
      assertEquals(
          List.of(15, 50),
          response.porcoes().stream().map(ProdutoPorcaoResponse::quantidade).toList());

      verify(produtoRepository).findProdutoById(produto.getId());
      verify(produtoImagemService).gerarUrlMedia(produto.getImagem());
    }

    @Test
    void deveLancarNaoEncontradoSemGerarUrlQuandoProdutoNaoEncontrado() {
      UUID id = UUID.randomUUID();
      when(produtoRepository.findProdutoById(id)).thenReturn(Optional.empty());

      assertThrows(NaoEncontradoException.class, () -> produtoService.buscarProduto(id));

      verifyNoInteractions(produtoImagemService);
    }
  }

  private Produto criarProduto(List<ProdutoPorcao> porcoes) {
    Categoria categoria = CategoriaMock.criarCategoriaPadrao();
    Produto produto = ProdutoMock.criarProdutoPadrao(categoria);

    produto.removerPorcoes();
    produto.adicionarPorcoes(porcoes);

    return produto;
  }

  private Produto criarProduto(String nome, boolean disponivel, Categoria categoria) {
    Produto produto = ProdutoMock.criarProdutoPadrao(categoria);
    produto.setNome(nome);
    produto.setDisponivel(disponivel);
    return produto;
  }

  private Pageable criarPaginacao(int page, int size) {
    return PageRequest.of(
        page, size, Sort.by(Sort.Order.desc("disponivel"), Sort.Order.asc("nome")));
  }
}
