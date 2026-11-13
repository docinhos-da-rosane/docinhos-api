package br.com.projeto.docinhos.modules.admin.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.domain.model.Categoria;
import br.com.projeto.docinhos.domain.model.Produto;
import br.com.projeto.docinhos.domain.model.ProdutoImagem;
import br.com.projeto.docinhos.domain.model.ProdutoPorcao;
import br.com.projeto.docinhos.domain.repository.ProdutoRepository;
import br.com.projeto.docinhos.modules.admin.dto.request.ProdutoDisponibilidadeRequest;
import br.com.projeto.docinhos.modules.admin.dto.request.ProdutoRequest;
import br.com.projeto.docinhos.modules.admin.dto.response.ProdutoDetalhadoResponse;
import br.com.projeto.docinhos.modules.admin.dto.response.ProdutoPorcaoResponse;
import br.com.projeto.docinhos.modules.admin.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.modules.admin.dto.response.ProdutoResumoResponse;
import br.com.projeto.docinhos.modules.admin.mapper.CategoriaMapper;
import br.com.projeto.docinhos.modules.admin.mapper.ProdutoMapper;
import br.com.projeto.docinhos.modules.admin.mapper.ProdutoPorcaoMapper;
import br.com.projeto.docinhos.modules.admin.service.ProdutoImagemService;
import br.com.projeto.docinhos.shared.dto.PageResponse;
import br.com.projeto.docinhos.shared.exception.NaoEncontradoException;
import br.com.projeto.docinhos.shared.mapper.PageMapper;
import br.com.projeto.docinhos.shared.mocks.CategoriaMock;
import br.com.projeto.docinhos.shared.mocks.ProdutoMock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InOrder;
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
      ProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();
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
      ProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();
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
      ProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();
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
  class AtualizarProdutoTests {

    @Test
    void deveAtualizarDadosSemSubstituirImagemQuandoNaoEnviada() {
      Produto produto = criarProduto(List.of(ProdutoMock.criarProdutoPorcaoPadrao()));
      ProdutoImagem imagemAnterior = produto.getImagem();
      ProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();

      when(produtoRepository.findProdutoById(produto.getId())).thenReturn(Optional.of(produto));
      when(categoriaService.buscarCategoriaPorId(request.categoriaId()))
          .thenReturn(produto.getCategoria());

      produtoService.atualizarProduto(produto.getId(), null, request);

      assertEquals(request.nome(), produto.getNome());
      assertSame(imagemAnterior, produto.getImagem());
      verify(produtoRepository).flush();
      verifyNoInteractions(produtoImagemService);
    }

    @Test
    void deveSubstituirImagemEDeletarAnterior() {
      Produto produto = criarProduto(List.of(ProdutoMock.criarProdutoPorcaoPadrao()));
      ProdutoImagem imagemAnterior = produto.getImagem();
      ProdutoImagem imagemNova = ProdutoMock.criarProdutoImagemPadrao();
      MockMultipartFile arquivo = ProdutoMock.criaMultiPartImagemPadrao();
      ProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();

      when(produtoRepository.findProdutoById(produto.getId())).thenReturn(Optional.of(produto));
      when(categoriaService.buscarCategoriaPorId(request.categoriaId()))
          .thenReturn(produto.getCategoria());
      when(produtoImagemService.armazenar(arquivo)).thenReturn(imagemNova);

      produtoService.atualizarProduto(produto.getId(), arquivo, request);

      assertSame(imagemNova, produto.getImagem());
      verify(produtoRepository).flush();
      verify(produtoImagemService).deletar(imagemAnterior);
      verify(produtoImagemService, never()).rollback(any());
    }

    @Test
    void devePropagarFalhaNaPersistenciaDeletarImagemNovaRestaurarImagemAntiga() {
      Produto produto = criarProduto(List.of(ProdutoMock.criarProdutoPorcaoPadrao()));
      ProdutoImagem imagemAnterior = produto.getImagem();
      ProdutoImagem imagemNova = ProdutoMock.criarProdutoImagemPadrao();
      MockMultipartFile arquivo = ProdutoMock.criaMultiPartImagemPadrao();
      ProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();

      when(produtoRepository.findProdutoById(produto.getId())).thenReturn(Optional.of(produto));
      when(categoriaService.buscarCategoriaPorId(request.categoriaId()))
          .thenReturn(produto.getCategoria());
      when(produtoImagemService.armazenar(arquivo)).thenReturn(imagemNova);
      doThrow(new RuntimeException("Erro ao persistir")).when(produtoRepository).flush();

      assertThrows(
          RuntimeException.class,
          () -> produtoService.atualizarProduto(produto.getId(), arquivo, request));

      verify(produtoImagemService, times(1)).restaurar(imagemAnterior.getIdArmazenamento());
      verify(produtoImagemService, times(1)).deletar(imagemAnterior);
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
      assertEquals(produto.getCategoria().getNome(), response.categoria().nome());
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

  @Nested
  class AlterarDisponibilidadeTests {

    @Test
    void deveDefinirEstadoSolicitadoComSucesso() {
      Produto produto = criarProduto(List.of());
      produto.setDisponivel(false);

      when(produtoRepository.findProdutoById(produto.getId())).thenReturn(Optional.of(produto));

      produtoService.alterarDisponibilidade(
          produto.getId(), new ProdutoDisponibilidadeRequest(true));

      assertTrue(produto.isDisponivel());
      verify(produtoRepository).findProdutoById(produto.getId());
    }

    @Test
    void deveManterEstadoQuandoSolicitacaoForRepetida() {
      Produto produto = criarProduto(List.of());
      produto.setDisponivel(false);

      when(produtoRepository.findProdutoById(produto.getId())).thenReturn(Optional.of(produto));

      produtoService.alterarDisponibilidade(
          produto.getId(), new ProdutoDisponibilidadeRequest(false));

      assertFalse(produto.isDisponivel());
    }

    @Test
    void deveLancarNaoEncontradoSemAlterarProduto() {
      UUID id = UUID.randomUUID();
      when(produtoRepository.findProdutoById(id)).thenReturn(Optional.empty());

      assertThrows(
          NaoEncontradoException.class,
          () ->
              produtoService.alterarDisponibilidade(id, new ProdutoDisponibilidadeRequest(false)));
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

  @Nested
  class DeletarProdutoTests {

    @Test
    void deveExcluirProdutoComSucesso() {
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      Produto produto = ProdutoMock.criarProdutoPadrao(categoria);
      UUID id = produto.getId();

      when(produtoRepository.findProdutoById(id)).thenReturn(Optional.of(produto));

      produtoService.deletarProduto(id);

      InOrder ordem = inOrder(produtoRepository, produtoImagemService);
      ordem.verify(produtoRepository).findProdutoById(id);
      ordem.verify(produtoRepository).delete(produto);
      ordem.verify(produtoImagemService).deletar(produto.getImagem());
      ordem.verify(produtoRepository).flush();
      verify(produtoImagemService, never()).restaurar(any());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
      UUID id = UUID.randomUUID();

      when(produtoRepository.findProdutoById(id)).thenReturn(Optional.empty());

      assertThrows(NaoEncontradoException.class, () -> produtoService.deletarProduto(id));

      verify(produtoRepository, never()).delete(any(Produto.class));
      verify(produtoRepository, never()).flush();
      verifyNoInteractions(produtoImagemService);
    }

    @Test
    void deveManterImagemQuandoDeleteDoProdutoFalhar() {
      UUID id = UUID.randomUUID();
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      Produto produto = ProdutoMock.criarProdutoPadrao(categoria);
      RuntimeException falhaOriginal = new RuntimeException("falha ao excluir produto");

      when(produtoRepository.findProdutoById(id)).thenReturn(Optional.of(produto));
      doThrow(falhaOriginal).when(produtoRepository).delete(produto);

      RuntimeException exception =
          assertThrows(RuntimeException.class, () -> produtoService.deletarProduto(id));

      assertSame(falhaOriginal, exception);
      verifyNoInteractions(produtoImagemService);
      verify(produtoRepository, never()).flush();
    }

    @Test
    void deveRestaurarImagemQuandoExclusaoNoCloudinaryFalhar() {
      UUID id = UUID.randomUUID();
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      Produto produto = ProdutoMock.criarProdutoPadrao(categoria);
      ProdutoImagem imagem = produto.getImagem();

      RuntimeException falhaOriginal = new RuntimeException("falha no Cloudinary");

      when(produtoRepository.findProdutoById(id)).thenReturn(Optional.of(produto));
      doThrow(falhaOriginal).when(produtoImagemService).deletar(imagem);

      RuntimeException exception =
          assertThrows(RuntimeException.class, () -> produtoService.deletarProduto(id));

      assertSame(falhaOriginal, exception);
      InOrder ordem = inOrder(produtoRepository, produtoImagemService);
      ordem.verify(produtoRepository).delete(produto);
      ordem.verify(produtoImagemService).deletar(imagem);
      ordem.verify(produtoImagemService).restaurar(imagem.getIdArmazenamento());
      verify(produtoRepository, never()).flush();
    }

    @Test
    void deveRestaurarImagemQuandoFlushFalhar() {
      UUID id = UUID.randomUUID();
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      Produto produto = ProdutoMock.criarProdutoPadrao(categoria);
      ProdutoImagem imagem = produto.getImagem();

      RuntimeException falhaOriginal = new RuntimeException("falha no flush");

      when(produtoRepository.findProdutoById(id)).thenReturn(Optional.of(produto));
      doThrow(falhaOriginal).when(produtoRepository).flush();

      RuntimeException exception =
          assertThrows(RuntimeException.class, () -> produtoService.deletarProduto(id));

      assertSame(falhaOriginal, exception);
      InOrder ordem = inOrder(produtoRepository, produtoImagemService);
      ordem.verify(produtoRepository).delete(produto);
      ordem.verify(produtoImagemService).deletar(imagem);
      ordem.verify(produtoRepository).flush();
      ordem.verify(produtoImagemService).restaurar(imagem.getIdArmazenamento());
    }

    @Test
    void devePreservarFalhaOriginalQuandoRestauracaoAposErroCloudinaryFalhar() {
      UUID id = UUID.randomUUID();
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      Produto produto = ProdutoMock.criarProdutoPadrao(categoria);
      ProdutoImagem imagem = produto.getImagem();

      RuntimeException falhaOriginal = new RuntimeException("falha no Cloudinary");
      RuntimeException falhaRestauracao = new RuntimeException("falha na restauração");

      when(produtoRepository.findProdutoById(id)).thenReturn(Optional.of(produto));
      doThrow(falhaOriginal).when(produtoImagemService).deletar(imagem);
      doThrow(falhaRestauracao).when(produtoImagemService).restaurar(imagem.getIdArmazenamento());

      RuntimeException exception =
          assertThrows(RuntimeException.class, () -> produtoService.deletarProduto(id));

      assertSame(falhaOriginal, exception);
      verify(produtoImagemService).restaurar(imagem.getIdArmazenamento());
    }
  }
}
