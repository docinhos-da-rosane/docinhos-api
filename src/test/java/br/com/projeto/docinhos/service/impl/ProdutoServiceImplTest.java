package br.com.projeto.docinhos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import br.com.projeto.docinhos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

  @Mock private ProdutoRepository produtoRepository;

  @Mock private CategoriaServiceImpl categoriaService;

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
            categoriaService, produtoRepository, produtoMapper, produtoPorcaoMapper);
  }

  @Nested
  class CriarProdutoTest {

    @Test
    void deveCriarProdutoComSucesso() {
      CriarProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();
      Produto produtoSalvo = ProdutoMock.criarProdutoPadrao(categoria);

      when(categoriaService.buscarCategoriaPorId(any())).thenReturn(categoria);
      when(produtoRepository.save(any())).thenReturn(produtoSalvo);

      ProdutoResponse response = produtoService.criarProduto(request);

      assertEquals(produtoSalvo.getId(), response.id());
      assertEquals(produtoSalvo.getCategoria().getId(), response.categoria().id());
      assertEquals(produtoSalvo.getPorcoes().size(), response.porcoes().size());
    }

    @Test
    void deveLancarNaoEncontradoExceptionQuandoCategoriaNaoExistir() {
      CriarProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();

      when(categoriaService.buscarCategoriaPorId(any()))
          .thenThrow(new NaoEncontradoException("Categoria não encontrada"));

      assertThrows(
          NaoEncontradoException.class,
          () -> {
            produtoService.criarProduto(request);
          });
    }
  }
}
