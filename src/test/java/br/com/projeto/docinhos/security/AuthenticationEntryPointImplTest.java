package br.com.projeto.docinhos.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class AuthenticationEntryPointImplTest {

  @Mock private SecurityErrorResponseService errorResponseService;

  @InjectMocks private AuthenticationEntryPointImpl authenticationEntryPoint;

  @Nested
  class CommenceTests {

    @Test
    void
        deveEscreverErroCredenciaisInvalidasQuandoExcecaoForInternalAuthenticationServiceException()
            throws IOException, ServletException {
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      AuthenticationException exception =
          new InternalAuthenticationServiceException("falha interna de autenticacao");

      authenticationEntryPoint.commence(request, response, exception);

      verify(errorResponseService).escreverErroCredenciaisInvalidas(response, request);
    }

    @Test
    void deveUsarEntryPointPadraoQuandoExcecaoNaoForInternalAuthenticationServiceException()
        throws IOException, ServletException {
      AuthenticationException exception = new BadCredentialsException("credenciais invalidas");
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();

      authenticationEntryPoint.commence(request, response, exception);

      verify(errorResponseService, never()).escreverErroCredenciaisInvalidas(response, request);
      assertEquals(403, response.getStatus());
    }
  }
}
