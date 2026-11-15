package br.com.projeto.docinhos.modules.publico.controller.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.projeto.docinhos.modules.publico.dto.response.ProdutoResumoPublicoResponse;
import br.com.projeto.docinhos.modules.publico.service.ProdutoPublicoService;
import br.com.projeto.docinhos.security.JwtAuthenticationFilter;
import br.com.projeto.docinhos.shared.dto.PageResponse;
import br.com.projeto.docinhos.shared.mocks.ProdutoMock;
import br.com.projeto.docinhos.shared.utils.TestUtils;
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
    controllers = ProdutoPublicoControllerImpl.class,
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProdutoPublicoControllerImplTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ProdutoPublicoService produtoPublicoService;

  private static final String PRODUTO_PUBLICO_URL = "/publico/produtos";

  @Nested
  class ListarProdutosTests {

    @Test
    void deveRetornarCatalogoPreenchidoComSucesso() throws Exception {
      ProdutoResumoPublicoResponse resumo = ProdutoMock.criarProdutoResumoPublicoResponsePadrao();
      PageResponse<ProdutoResumoPublicoResponse> response =
          new PageResponse<>(List.of(resumo), 0, 6, 1, 1);

      when(produtoPublicoService.listarProdutos(0, 6)).thenReturn(response);

      mockMvc
          .perform(get(PRODUTO_PUBLICO_URL).queryParam("page", "0").queryParam("size", "6"))
          .andExpect(status().isOk())
          .andExpect(content().json(TestUtils.toJson(response)));

      verify(produtoPublicoService).listarProdutos(0, 6);
    }

    @Test
    void deveRetornarCatalogoVazioComSucesso() throws Exception {
      PageResponse<ProdutoResumoPublicoResponse> response =
          new PageResponse<>(List.of(), 0, 6, 0, 0);

      when(produtoPublicoService.listarProdutos(0, 6)).thenReturn(response);

      mockMvc
          .perform(get(PRODUTO_PUBLICO_URL).queryParam("page", "0").queryParam("size", "6"))
          .andExpect(status().isOk())
          .andExpect(content().json(TestUtils.toJson(response)));
    }
  }
}
