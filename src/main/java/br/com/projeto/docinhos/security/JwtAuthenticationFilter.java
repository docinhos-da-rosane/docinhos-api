package br.com.projeto.docinhos.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final SecurityErrorResponseService errorResponseService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = extrairToken(request);

    if (Objects.isNull(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    String email = extrairEmail(request, response, token);

    if (Objects.isNull(email)) {
      filterChain.doFilter(request, response);
      return;
    }

    if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
      filterChain.doFilter(request, response);
      return;
    }

    UserDetails usuario = userDetailsService.loadUserByUsername(email);

    if (!jwtService.tokenValido(token, usuario)) {
      errorResponseService.escreverErroCredenciaisInvalidas(response, request);
      filterChain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private String extrairEmail(
      HttpServletRequest request, HttpServletResponse response, String token) {
    try {

      return jwtService.extrairEmail(token);

    } catch (ExpiredJwtException e) {
      errorResponseService.escreverErroCredenciaisExpiradas(response, request);

    } catch (JwtException e) {
      errorResponseService.escreverErroCredenciaisInvalidas(response, request);
    }

    return null;
  }

  private String extrairToken(HttpServletRequest request) {

    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (Objects.isNull(authorization) || !authorization.startsWith("Bearer ")) {
      return null;
    }

    return authorization.substring(7);
  }
}
