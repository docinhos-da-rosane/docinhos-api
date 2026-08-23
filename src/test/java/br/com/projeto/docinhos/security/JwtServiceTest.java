package br.com.projeto.docinhos.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

  private static final String SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";
  private static final long EXPIRACAO_MILLIS = 3600000L;

  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService(SECRET, EXPIRACAO_MILLIS);
  }

  @Nested
  class GerarTokenTests {

    @Test
    void deveGerarTokenComClaimsBasicasQuandoUsuarioValido() {
      SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
      UserDetails usuario = criarUsuario("maria@docinhos.com");

      String token = jwtService.gerarToken(usuario);

      assertNotNull(token);
      assertFalse(token.isBlank());

      Claims claims =
          Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

      assertEquals(usuario.getUsername(), claims.getSubject());
      assertNotNull(claims.getIssuedAt());
      assertNotNull(claims.getExpiration());
      assertEquals(
          EXPIRACAO_MILLIS, claims.getExpiration().getTime() - claims.getIssuedAt().getTime());
    }
  }

  @Nested
  class TokenValidoTests {

    @ParameterizedTest
    @CsvSource({"maria@gmail.com,true", "joao@hotmail.com,false", "MARIA_DOCINHOS@email.com,false"})
    void deveRetornarResultadoEsperadoQuandoCompararUsuarioDoToken(
        String usernameComparacao, boolean esperado) {

      String token = jwtService.gerarToken(criarUsuario("maria@gmail.com"));
      UserDetails usuarioComparacao = criarUsuario(usernameComparacao);

      boolean resultado = jwtService.tokenValido(token, usuarioComparacao);

      assertEquals(esperado, resultado);
    }
  }

  @Nested
  class ExtrairEmailTests {

    @ParameterizedTest
    @ValueSource(strings = {"ana@gmail.com", "pedro@gmail.com"})
    void deveExtrairEmailCorretamenteQuandoTokenValido(String email) {
      String token = jwtService.gerarToken(criarUsuario(email));

      String emailExtraido = jwtService.extrairEmail(token);

      assertEquals(email, emailExtraido);
    }

    @Test
    void deveLancarExcecaoQuandoTokenInvalido() {
      assertThrows(JwtException.class, () -> jwtService.extrairEmail("token-invalido"));
    }
  }

  private UserDetails criarUsuario(String username) {
    return User.withUsername(username).password("123456").authorities("USER").build();
  }
}
