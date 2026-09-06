package br.com.projeto.docinhos.controller.impl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.projeto.docinhos.dto.request.AuthRequest;
import br.com.projeto.docinhos.dto.response.AuthResponse;
import br.com.projeto.docinhos.exception.ErroInternoException;
import br.com.projeto.docinhos.exception.NaoEncontradoException;
import br.com.projeto.docinhos.mocks.AuthMock;
import br.com.projeto.docinhos.security.JwtAuthenticationFilter;
import br.com.projeto.docinhos.service.AuthService;
import br.com.projeto.docinhos.utils.TestUtils;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@WebMvcTest(
    controllers = AuthControllerImpl.class,
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerImplTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AuthService authService;

  private static final String AUTH_URL = "/auth";
  private static final String LOGAR_URL = AUTH_URL + "/login";

  @Nested
  class LogarTests {

    @Test
    void deveRetornarAuthResponseQuandoServicoLogarComSucesso() throws Exception {
      AuthRequest request = AuthMock.criarAuthRequestPadrao();
      AuthResponse responseEsperada = AuthMock.criarAuthResponsePadrao();

      when(authService.logar(request)).thenReturn(responseEsperada);

      mockMvc
          .perform(
              post(LOGAR_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(TestUtils.toJson(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.token").value(responseEsperada.token()));
    }

    @ParameterizedTest
    @MethodSource("authRequestProvider")
    void deveLancarExcecaoQuandoRequestInvalido(AuthRequest authRequest) throws Exception {
      mockMvc
          .perform(
              post(LOGAR_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(TestUtils.toJson(authRequest)))
          .andExpect(status().isBadRequest());
    }

    private static Stream<AuthRequest> authRequestProvider() {
      return Stream.of(
          AuthMock.criarAuthRequest().email(null).build(),
          AuthMock.criarAuthRequest().email("").build(),
          AuthMock.criarAuthRequest().email("a".repeat(101) + "@docinhos.com").build(),
          AuthMock.criarAuthRequest().senha(null).build(),
          AuthMock.criarAuthRequest().senha("").build(),
          AuthMock.criarAuthRequest().senha("a".repeat(101)).build());
    }

    @ParameterizedTest
    @MethodSource("excecoesProvider")
    void devePropagarExcecaoQuandoLogarFalharComExcecaoEspecifica(
        Exception excecao, ResultMatcher statusEsperado) throws Exception {
      AuthRequest request = AuthMock.criarAuthRequestPadrao();
      when(authService.logar(request)).thenThrow(excecao);

      mockMvc
          .perform(
              post(LOGAR_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(TestUtils.toJson(request)))
          .andExpect(statusEsperado);
    }

    private static Stream<Arguments> excecoesProvider() {
      return Stream.of(
          Arguments.of(new NaoEncontradoException("usuario nao encontrado"), status().isNotFound()),
          Arguments.of(
              new ErroInternoException("erro interno", null), status().isInternalServerError()),
          Arguments.of(new RuntimeException("erro generico"), status().isInternalServerError()));
    }
  }
}
