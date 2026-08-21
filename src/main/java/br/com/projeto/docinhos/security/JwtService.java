package br.com.projeto.docinhos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final SecretKey secretKey;
  private final long expiration;

  public JwtService(
      @Value("${spring.security.jwt.secret}") String secret,
      @Value("${spring.security.jwt.expiration}") long expiration) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.expiration = expiration;
  }

  public String gerarToken(UserDetails usuario) {
    Instant agora = Instant.now();

    return Jwts.builder()
        .subject(usuario.getUsername())
        .issuedAt(Date.from(agora))
        .expiration(Date.from(agora.plusMillis(expiration)))
        .signWith(secretKey)
        .compact();
  }

  public boolean tokenValido(String token, UserDetails usuario) {
    String email = extrairEmail(token);
    return email.equals(usuario.getUsername());
  }

  public String extrairEmail(String token) {
    return extrairClaims(token).getSubject();
  }

  private Claims extrairClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }
}
