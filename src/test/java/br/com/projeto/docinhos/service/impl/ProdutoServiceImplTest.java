package br.com.projeto.docinhos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.exception.NaoEncontradoException;
import br.com.projeto.docinhos.mapper.CategoriaMapper;
import br.com.projeto.docinhos.mapper.ProdutoMapper;
import br.com.projeto.docinhos.mapper.ProdutoPorcaoMapper;
import br.com.projeto.docinhos.mocks.CategoriaMock;
import br.com.projeto.docinhos.mocks.ProdutoMock;
import br.com.projeto.docinhos.model.Categoria;
import br.com.projeto.docinhos.model.Produto;
import br.com.projeto.docinhos.model.ProdutoImagem;
import br.com.projeto.docinhos.repository.ProdutoRepository;
import br.com.projeto.docinhos.service.ProdutoImagemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

  private ProdutoServiceImpl produtoService;

  @BeforeEach
  void setUp() {
    categoriaMapper = Mappers.getMapper(CategoriaMapper.class);
    produtoPorcaoMapper = Mappers.getMapper(ProdutoPorcaoMapper.class);
    produtoMapper = Mappers.getMapper(ProdutoMapper.class);

    ReflectionTestUtils.setField(produtoMapper, "categoriaMapper", categoriaMapper);
    ReflectionTestUtils.setField(produtoMapper, "produtoPorcaoMapper", produtoPorcaoMapper);

    produtoService =
        new ProdutoServiceImpl(
            categoriaService,
            produtoImagemService,
            produtoRepository,
            produtoMapper,
            produtoPorcaoMapper);
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
}
