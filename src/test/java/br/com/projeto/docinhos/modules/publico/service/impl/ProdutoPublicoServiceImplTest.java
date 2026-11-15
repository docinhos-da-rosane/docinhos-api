package br.com.projeto.docinhos.modules.publico.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.domain.model.Produto;
import br.com.projeto.docinhos.domain.model.ProdutoPorcao;
import br.com.projeto.docinhos.domain.repository.ProdutoRepository;
import br.com.projeto.docinhos.modules.publico.dto.response.ProdutoResumoPublicoResponse;
import br.com.projeto.docinhos.modules.publico.mapper.ProdutoPublicoMapper;
import br.com.projeto.docinhos.modules.publico.service.ProdutoImagemPublicoService;
import br.com.projeto.docinhos.shared.dto.PageResponse;
import br.com.projeto.docinhos.shared.mapper.PageMapper;
import br.com.projeto.docinhos.shared.mocks.ProdutoMock;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ProdutoPublicoServiceImplTest {

  @Mock private ProdutoRepository produtoRepository;
  @Mock private ProdutoImagemPublicoService produtoImagemPublicoService;

  private ProdutoPublicoServiceImpl produtoService;

  @BeforeEach
  void setUp() {
    ProdutoPublicoMapper mapper = Mappers.getMapper(ProdutoPublicoMapper.class);
    PageMapper pageMapper = Mappers.getMapper(PageMapper.class);
    produtoService =
        new ProdutoPublicoServiceImpl(
            produtoRepository, mapper, produtoImagemPublicoService, pageMapper);
  }

  @Nested
  class ListarProdutosTests {

    @Test
    void deveListarProdutosComPrecoMinimo() {
      String urlMenor = "https://example.com/800x450.jpg";
      ProdutoPorcao porcaoMenorPreco = ProdutoMock.criarProdutoPorcao(12, "35.00");
      Produto brigadeiro =
          ProdutoMock.criarProdutoPadrao(
              "Brigadeiro",
              true,
              List.of(ProdutoMock.criarProdutoPorcao(25, "65.00"), porcaoMenorPreco));

      when(produtoImagemPublicoService.gerarUrlMenor(any())).thenReturn(urlMenor);

      Pageable pageable = PageRequest.of(0, 1);

      Page<Produto> produtos = new PageImpl<>(List.of(brigadeiro), pageable, 3);

      when(produtoRepository.findAll(any(Pageable.class))).thenReturn(produtos);

      PageResponse<?> resposta = produtoService.listarProdutos(0, 1);

      assertEquals(1, resposta.content().size());
      assertEquals(0, resposta.page());
      assertEquals(1, resposta.size());
      assertEquals(3, resposta.totalElements());
      assertEquals(3, resposta.totalPages());

      ProdutoResumoPublicoResponse resumo =
          (ProdutoResumoPublicoResponse) resposta.content().getFirst();
      assertEquals(porcaoMenorPreco.getPreco(), resumo.porcaoMenorPreco().preco());
      assertEquals(porcaoMenorPreco.getQuantidade(), resumo.porcaoMenorPreco().quantidade());
      assertEquals(brigadeiro.getCategoria().getNome(), resumo.categoria());
      assertEquals(urlMenor, resumo.imagemMenorUrl());
    }

    @Test
    void deveRetornarListaVaziaComSucesso() {
      Pageable pageable = PageRequest.of(1, 6);
      when(produtoRepository.findAll(any(Pageable.class)))
          .thenReturn(new PageImpl<>(List.of(), pageable, 0));

      PageResponse<?> resposta = produtoService.listarProdutos(1, 6);

      assertEquals(List.of(), resposta.content());
      assertEquals(1, resposta.page());
      assertEquals(6, resposta.size());
      assertEquals(0, resposta.totalElements());
      assertEquals(0, resposta.totalPages());
    }

    @Test
    void deveOrdenarPorDisponibilidadeENome() {
      ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
      Pageable pageable = PageRequest.of(1, 6);

      when(produtoRepository.findAll(any(Pageable.class)))
          .thenReturn(new PageImpl<>(List.of(), pageable, 0));

      produtoService.listarProdutos(0, 1);

      verify(produtoRepository).findAll(pageableCaptor.capture());
      List<Sort.Order> ordem = pageableCaptor.getValue().getSort().stream().toList();
      assertEquals(2, ordem.size());
      assertEquals(Sort.Order.desc("disponivel"), ordem.get(0));
      assertEquals(Sort.Order.asc("nome"), ordem.get(1));
    }
  }
}
