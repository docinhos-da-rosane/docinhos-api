package br.com.projeto.docinhos.controller.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.projeto.docinhos.dto.response.CategoriaResponse;
import br.com.projeto.docinhos.enums.CodeError;
import br.com.projeto.docinhos.mocks.CategoriaMock;
import br.com.projeto.docinhos.security.JwtAuthenticationFilter;
import br.com.projeto.docinhos.service.CategoriaService;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = CategoriaControllerImpl.class,
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CategoriaControllerImplTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CategoriaService categoriaService;

  private static final String CATEGORIA_URL = "/categorias";

  @Nested
  class BuscarCategoriasTests {

    @Test
    void deveRetornarCategoriasQuandoBuscarCategoriasComSucesso() throws Exception {
      CategoriaResponse categoria1 = CategoriaMock.criarCategoriaResponsePadrao();
      CategoriaResponse categoria2 = CategoriaMock.criarCategoriaResponsePadrao();
      List<CategoriaResponse> categorias = List.of(categoria1, categoria2);

      when(categoriaService.buscarCategorias()).thenReturn(categorias);

      mockMvc
          .perform(get(CATEGORIA_URL))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(2))
          .andExpect(jsonPath("$[0].id").value(categoria1.id().toString()))
          .andExpect(jsonPath("$[0].nome").value(categoria1.nome()))
          .andExpect(jsonPath("$[1].id").value(categoria2.id().toString()))
          .andExpect(jsonPath("$[1].nome").value(categoria2.nome()));
    }

    @Test
    void devePropagarExcecaoQuandoBuscarCategoriasFalhar() throws Exception {
      when(categoriaService.buscarCategorias()).thenThrow(new RuntimeException("erro generico"));

      mockMvc
          .perform(get(CATEGORIA_URL))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.codeError").value(CodeError.ERRO_INTERNO.name()));
    }
  }
}
