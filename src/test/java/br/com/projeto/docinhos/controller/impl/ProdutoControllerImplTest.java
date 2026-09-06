package br.com.projeto.docinhos.controller.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.mocks.ProdutoMock;
import br.com.projeto.docinhos.security.JwtAuthenticationFilter;
import br.com.projeto.docinhos.service.ProdutoService;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(
    controllers = ProdutoControllerImpl.class,
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProdutoControllerImplTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private ProdutoService produtoService;

  private static final String PRODUTO_URL = "/produtos";

  @Nested
  class CadastrarProdutoTests {

    @Test
    void deveCadastrarProdutoComSucesso() throws Exception {
      CriarProdutoRequest request = ProdutoMock.criarProdutoRequestPadrao();
      ProdutoResponse response = ProdutoMock.criarProdutoResponsePadrao();

      when(produtoService.criarProduto(request)).thenReturn(response);

      MockMultipartFile produtoPart = criarProdutoPart(jsonToString(request));

      mockMvc
          .perform((multipart(PRODUTO_URL).file(produtoPart)))
          .andExpect(status().isCreated())
          .andExpect(content().json(jsonToString(response)));
    }

    @ParameterizedTest
    @MethodSource("requestInvalidoProvider")
    void deveRetornarBadRequestQuandoCadastrarProdutoComDadosInvalidos(
        CriarProdutoRequest requestInvalido) throws Exception {
      MockMultipartFile produtoPart = criarProdutoPart(jsonToString(requestInvalido));

      mockMvc
          .perform((multipart(PRODUTO_URL).file(produtoPart)))
          .andExpect(status().isBadRequest());
    }

    static Stream<CriarProdutoRequest> requestInvalidoProvider() {
      return Stream.of(
          ProdutoMock.criarProdutoRequest().nome("").build(),
          ProdutoMock.criarProdutoRequest().nome("a".repeat(101)).build(),
          ProdutoMock.criarProdutoRequest().descricao("").build(),
          ProdutoMock.criarProdutoRequest().descricao("a".repeat(501)).build(),
          ProdutoMock.criarProdutoRequest().categoriaId(null).build(),
          ProdutoMock.criarProdutoRequest().porcoes(null).build(),
          ProdutoMock.criarProdutoRequest().porcoes(List.of()).build(),
          ProdutoMock.criarProdutoRequest()
              .porcoes(List.of(ProdutoMock.criarProdutoPorcaoRequest().quantidade(null).build()))
              .build(),
          ProdutoMock.criarProdutoRequest()
              .porcoes(List.of(ProdutoMock.criarProdutoPorcaoRequest().quantidade(0).build()))
              .build(),
          ProdutoMock.criarProdutoRequest()
              .porcoes(List.of(ProdutoMock.criarProdutoPorcaoRequest().quantidade(-1).build()))
              .build(),
          ProdutoMock.criarProdutoRequest()
              .porcoes(List.of(ProdutoMock.criarProdutoPorcaoRequest().preco(null).build()))
              .build(),
          ProdutoMock.criarProdutoRequest()
              .porcoes(
                  List.of(
                      ProdutoMock.criarProdutoPorcaoRequest()
                          .preco(new BigDecimal("0.00"))
                          .build()))
              .build(),
          ProdutoMock.criarProdutoRequest()
              .porcoes(
                  List.of(
                      ProdutoMock.criarProdutoPorcaoRequest()
                          .preco(new BigDecimal("-1.00"))
                          .build()))
              .build());
    }
  }

  private MockMultipartFile criarProdutoPart(String content) {
    return new MockMultipartFile(
        "produto",
        null,
        MediaType.APPLICATION_JSON_VALUE,
        content.getBytes(StandardCharsets.UTF_8));
  }

  private String jsonToString(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao converter objeto para JSON", e);
    }
  }
}
