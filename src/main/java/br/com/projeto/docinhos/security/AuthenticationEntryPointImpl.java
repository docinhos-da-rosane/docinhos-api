package br.com.projeto.docinhos.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
  private final SecurityErrorResponseService errorResponseService;
  private final AuthenticationEntryPoint defaultEntryPoint = new Http403ForbiddenEntryPoint();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    if (deveUsarErroCustomizado(authException)) {
      errorResponseService.escreverErroCredenciaisInvalidas(response, request);
      return;
    }

    defaultEntryPoint.commence(request, response, authException);
  }

  private boolean deveUsarErroCustomizado(AuthenticationException authException) {
    return authException instanceof InternalAuthenticationServiceException;
  }
}
