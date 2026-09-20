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
import java.util.Optional;
import java.util.UUID;
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

      when(categoriaRepository.findAllByOrderByNomeAsc())
          .thenReturn(List.of(categoria1, categoria2));

      List<CategoriaResponse> resposta = service.buscarCategorias();

      assertEquals(2, resposta.size());
      verify(categoriaRepository).findAllByOrderByNomeAsc();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverCategorias() {
      when(categoriaRepository.findAllByOrderByNomeAsc()).thenReturn(List.of());

      List<CategoriaResponse> resposta = service.buscarCategorias();

      assertEquals(0, resposta.size());
      verify(categoriaRepository).findAllByOrderByNomeAsc();
    }

    @Test
    void devePropagarExcecaoQuandoBuscaFalhar() {
      when(categoriaRepository.findAllByOrderByNomeAsc())
          .thenThrow(new RuntimeException("erro ao buscar categorias"));

      assertThrows(RuntimeException.class, () -> service.buscarCategorias());
      verify(categoriaRepository).findAllByOrderByNomeAsc();
    }
  }

  @Nested
  class BuscarCategoriaPorIdTests {

    @Test
    void deveBuscarCategoriaPorIdComSucesso() {
      Categoria categoria = CategoriaMock.criarCategoriaPadrao();

      when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));

      Categoria response = service.buscarCategoriaPorId(categoria.getId());

      assertEquals(categoria.getId(), response.getId());
      assertEquals(categoria.getNome(), response.getNome());
      verify(categoriaRepository).findById(categoria.getId());
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoEncontrada() {
      UUID categoriaId = UUID.randomUUID();

      when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());

      assertThrows(RuntimeException.class, () -> service.buscarCategoriaPorId(categoriaId));
      verify(categoriaRepository).findById(categoriaId);
    }
  }
}
