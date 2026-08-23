package br.com.projeto.docinhos.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.mocks.AuthMock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @Mock private JwtService jwtService;
  @Mock private UserDetailsService userDetailsService;
  @Mock private SecurityErrorResponseService errorResponseService;
  @Mock private FilterChain filterChain;
  @InjectMocks private JwtAuthenticationFilter filter;

  @AfterEach
  void limparContexto() {
    SecurityContextHolder.clearContext();
  }

  @Nested
  class DoFilterInternalTests {

    @ParameterizedTest
    @MethodSource("authorizationSemBearerProvider")
    void deveSeguirFilterChainPadraoQuandoHeaderAuthorizationNaoContiverBearer(String authorization)
        throws Exception {
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();

      if (authorization != null) {
        request.addHeader(HttpHeaders.AUTHORIZATION, authorization);
      }

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(jwtService, never()).extrairEmail(any());
      verify(userDetailsService, never()).loadUserByUsername(any());
    }

    static Stream<String> authorizationSemBearerProvider() {
      return Stream.of(null, "", "Basic abc", "Token abc");
    }

    @Test
    void deveEscreverErroCredenciaisExpiradasQuandoTokenExpirado() throws Exception {
      String tokenExpirado = "token-expirado";

      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenExpirado);

      when(jwtService.extrairEmail(tokenExpirado))
          .thenThrow(Mockito.mock(ExpiredJwtException.class));

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(errorResponseService).escreverErroCredenciaisExpiradas(response, request);
      verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void deveEscreverErroCredenciaisInvalidasQuandoTokenInvalidoAoExtrairEmail() throws Exception {
      String tokenInvalido = "token-invalido";

      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenInvalido);

      when(jwtService.extrairEmail(tokenInvalido)).thenThrow(new JwtException("invalido"));

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(errorResponseService).escreverErroCredenciaisInvalidas(response, request);
      verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void deveSeguirFilterChainPadraoQuandoJaExistirAutenticacaoNoContexto() throws Exception {
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthMock.TOKEN_VALIDO);

      SecurityContextHolder.getContext()
          .setAuthentication(new UsernamePasswordAuthenticationToken("ja-autenticado", null));

      when(jwtService.extrairEmail(AuthMock.TOKEN_VALIDO)).thenReturn(AuthMock.EMAIL_VALIDO);

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(userDetailsService, never()).loadUserByUsername(any());
      verify(errorResponseService, never()).escreverErroCredenciaisInvalidas(any(), any());
      verify(errorResponseService, never()).escreverErroCredenciaisExpiradas(any(), any());
    }

    @Test
    void deveEscreverErroCredenciaisInvalidasQuandoTokenNaoForValidoParaUsuario() throws Exception {
      String tokenInvalido = "token-valido-outro-usuario";
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenInvalido);

      UserDetails usuario =
          User.withUsername(AuthMock.EMAIL_VALIDO)
              .password(AuthMock.SENHA_VALIDA)
              .authorities("USER")
              .build();

      when(jwtService.extrairEmail(tokenInvalido)).thenReturn(AuthMock.EMAIL_VALIDO);
      when(userDetailsService.loadUserByUsername(AuthMock.EMAIL_VALIDO)).thenReturn(usuario);
      when(jwtService.tokenValido(tokenInvalido, usuario)).thenReturn(false);

      filter.doFilterInternal(request, response, filterChain);

      verify(errorResponseService).escreverErroCredenciaisInvalidas(response, request);
      verify(filterChain).doFilter(request, response);
    }

    @Test
    void deveAutenticarUsuarioQuandoTokenForValido() throws Exception {
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AuthMock.TOKEN_VALIDO);

      UserDetails usuario =
          User.withUsername(AuthMock.EMAIL_VALIDO)
              .password(AuthMock.SENHA_VALIDA)
              .authorities("USER")
              .build();

      when(jwtService.extrairEmail(AuthMock.TOKEN_VALIDO)).thenReturn(AuthMock.EMAIL_VALIDO);
      when(userDetailsService.loadUserByUsername(AuthMock.EMAIL_VALIDO)).thenReturn(usuario);
      when(jwtService.tokenValido(AuthMock.TOKEN_VALIDO, usuario)).thenReturn(true);

      filter.doFilterInternal(request, response, filterChain);

      assertNotNull(SecurityContextHolder.getContext().getAuthentication());
      assertEquals(
          AuthMock.EMAIL_VALIDO, SecurityContextHolder.getContext().getAuthentication().getName());
      verify(filterChain).doFilter(request, response);
      verify(errorResponseService, never()).escreverErroCredenciaisInvalidas(any(), any());
      verify(errorResponseService, never()).escreverErroCredenciaisExpiradas(any(), any());
    }
  }
}
