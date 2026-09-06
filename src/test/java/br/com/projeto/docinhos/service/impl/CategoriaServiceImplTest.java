package br.com.projeto.docinhos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.dto.response.CategoriaResponse;
import br.com.projeto.docinhos.mapper.CategoriaMapper;
import br.com.projeto.docinhos.mapper.CategoriaMapperImpl;
import br.com.projeto.docinhos.mocks.CategoriaMock;
import br.com.projeto.docinhos.model.Categoria;
import br.com.projeto.docinhos.repository.CategoriaRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

  @Mock private CategoriaRepository categoriaRepository;

  private CategoriaMapper categoriaMapper;
  private CategoriaServiceImpl service;

  @BeforeEach
  void setUp() {
    categoriaMapper = new CategoriaMapperImpl();
    service = new CategoriaServiceImpl(categoriaRepository, categoriaMapper);
  }

  @Nested
  class BuscarCategoriasTests {

    @Test
    void deveBuscarCategoriasComSucesso() {
      Categoria categoria1 = CategoriaMock.criarCategoriaPadrao();
      Categoria categoria2 = CategoriaMock.criarCategoriaPadrao();

      when(categoriaRepository.findAll()).thenReturn(List.of(categoria1, categoria2));

      List<CategoriaResponse> resposta = service.buscarCategorias();

      assertEquals(2, resposta.size());
      verify(categoriaRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverCategorias() {
      when(categoriaRepository.findAll()).thenReturn(List.of());

      List<CategoriaResponse> resposta = service.buscarCategorias();

      assertEquals(0, resposta.size());
      verify(categoriaRepository).findAll();
    }

    @Test
    void devePropagarExcecaoQuandoBuscaFalhar() {
      when(categoriaRepository.findAll())
          .thenThrow(new RuntimeException("erro ao buscar categorias"));

      assertThrows(RuntimeException.class, () -> service.buscarCategorias());
      verify(categoriaRepository).findAll();
    }
  }
}
