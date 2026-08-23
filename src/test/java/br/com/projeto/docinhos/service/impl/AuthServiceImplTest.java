package br.com.projeto.docinhos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.dto.AuthRequest;
import br.com.projeto.docinhos.dto.AuthResponse;
import br.com.projeto.docinhos.security.JwtService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtService jwtService;
  @Mock private Authentication authentication;

  @InjectMocks private AuthServiceImpl authService;

  @Nested
  class LogarTests {

    @Test
    void deveRetornarAuthResponseComTokenQuandoCredenciaisForemValidas() {
      String email = "maria@docinhos.com";
      String senha = "123456";
      String tokenEsperado = "token-maria";

      AuthRequest request = new AuthRequest(email, senha);
      UserDetails usuario = User.withUsername(email).password(senha).authorities("USER").build();

      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
          .thenReturn(authentication);
      when(authentication.getPrincipal()).thenReturn(usuario);
      when(jwtService.gerarToken(usuario)).thenReturn(tokenEsperado);

      AuthResponse response = authService.logar(request);

      assertEquals(tokenEsperado, response.token());

      ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
          ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
      verify(authenticationManager).authenticate(captor.capture());

      UsernamePasswordAuthenticationToken authToken = captor.getValue();
      assertEquals(email, authToken.getPrincipal());
      assertEquals(senha, authToken.getCredentials());

      verify(jwtService).gerarToken(usuario);
    }

    @Test
    void deveLancarNullPointerExceptionQuandoPrincipalForNulo() {
      AuthRequest request = new AuthRequest("maria@docinhos.com", "123456");

      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
          .thenReturn(authentication);
      when(authentication.getPrincipal()).thenReturn(null);

      assertThrows(NullPointerException.class, () -> authService.logar(request));
      verify(jwtService, never()).gerarToken(any());
    }
  }
}
