package br.com.projeto.docinhos.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import br.com.projeto.docinhos.dto.response.ErrorResponse;
import br.com.projeto.docinhos.enums.CodeError;
import br.com.projeto.docinhos.exception.ErroInternoException;
import java.io.Writer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class SecurityErrorResponseServiceTest {

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private SecurityErrorResponseService service;

  @Nested
  class EscreverErroCredenciaisInvalidasTests {

    @Test
    void deveEscreverRespostaDeErroQuandoCredenciaisInvalidas() throws Exception {
      String uri = "/auth/login";

      int statusEsperado = 401;
      String charEncodingEsperado = "UTF-8";
      CodeError codeEsperado = CodeError.CREDENCIAIS_INVALIDAS;
      String mensagemEsperada = "Credenciais inválidas. Por favor, faça login novamente.";

      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI(uri);
      MockHttpServletResponse response = new MockHttpServletResponse();

      service.escreverErroCredenciaisInvalidas(response, request);

      assertEquals(statusEsperado, response.getStatus());
      assertEquals(charEncodingEsperado, response.getCharacterEncoding());

      ArgumentCaptor<ErrorResponse> captor = ArgumentCaptor.forClass(ErrorResponse.class);
      verify(objectMapper).writeValue(any(Writer.class), captor.capture());

      ErrorResponse erro = captor.getValue();
      assertEquals(codeEsperado, erro.codeError());
      assertEquals(mensagemEsperada, erro.mensagem());
      assertEquals(uri, erro.path());
      assertNotNull(erro.timestamp());
    }

    @Test
    void deveLancarErroInternoQuandoFalharAoEscreverCredenciaisInvalidas() throws Exception {
      String uri = "/auth/login";
      String mensagemEsperada = "Erro ao escrever a resposta de erro";
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI(uri);
      MockHttpServletResponse response = new MockHttpServletResponse();

      doThrow(new JacksonException("Falha ao serializar") {})
          .when(objectMapper)
          .writeValue(any(Writer.class), any(ErrorResponse.class));

      ErroInternoException exception =
          assertThrows(
              ErroInternoException.class,
              () -> service.escreverErroCredenciaisInvalidas(response, request));

      assertEquals(mensagemEsperada, exception.getMessage());
    }
  }

  @Nested
  class EscreverErroCredenciaisExpiradasTests {

    @Test
    void deveEscreverRespostaDeErroQuandoCredenciaisExpiradas() throws Exception {
      String uri = "/auth/teste";

      int statusEsperado = 401;
      String charEncodingEsperado = "UTF-8";
      CodeError codeEsperado = CodeError.CREDENCIAIS_EXPIRADAS;
      String mensagemEsperada = "Credenciais expiradas. Por favor, faça login novamente.";

      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI(uri);
      MockHttpServletResponse response = new MockHttpServletResponse();

      service.escreverErroCredenciaisExpiradas(response, request);

      assertEquals(statusEsperado, response.getStatus());
      assertEquals(charEncodingEsperado, response.getCharacterEncoding());

      ArgumentCaptor<ErrorResponse> captor = ArgumentCaptor.forClass(ErrorResponse.class);
      verify(objectMapper).writeValue(any(Writer.class), captor.capture());

      ErrorResponse erro = captor.getValue();
      assertEquals(codeEsperado, erro.codeError());
      assertEquals(mensagemEsperada, erro.mensagem());
      assertEquals(uri, erro.path());
      assertNotNull(erro.timestamp());
    }

    @Test
    void deveLancarErroInternoQuandoFalharAoEscreverCredenciaisExpiradas() throws Exception {
      String uri = "/auth/teste";

      String mensagemEsperada = "Erro ao escrever a resposta de erro";
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI(uri);
      MockHttpServletResponse response = new MockHttpServletResponse();

      doThrow(new JacksonException("Falha ao serializar") {})
          .when(objectMapper)
          .writeValue(any(Writer.class), any(ErrorResponse.class));

      ErroInternoException exception =
          assertThrows(
              ErroInternoException.class,
              () -> service.escreverErroCredenciaisExpiradas(response, request));

      assertEquals(mensagemEsperada, exception.getMessage());
    }
  }
}
