package br.com.projeto.docinhos.security;

import br.com.projeto.docinhos.dto.ErrorResponse;
import br.com.projeto.docinhos.enums.CodeError;
import br.com.projeto.docinhos.exception.ErroInternoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SecurityErrorResponseService {

  private final ObjectMapper objectMapper;

  public void escreverErroCredenciaisInvalidas(
      HttpServletResponse response, HttpServletRequest request) {
    escreverErro(
        response,
        request,
        HttpStatus.UNAUTHORIZED,
        CodeError.CREDENCIAIS_INVALIDAS,
        "Credenciais inválidas. Por favor, faça login novamente.");
  }

  public void escreverErroCredenciaisExpiradas(
      HttpServletResponse response, HttpServletRequest request) {
    escreverErro(
        response,
        request,
        HttpStatus.UNAUTHORIZED,
        CodeError.CREDENCIAIS_EXPIRADAS,
        "Credenciais expiradas. Por favor, faça login novamente.");
  }

  private void escreverErro(
      HttpServletResponse response,
      HttpServletRequest request,
      HttpStatus status,
      CodeError code,
      String message) {

    ErrorResponse error = new ErrorResponse(code, message, request.getRequestURI());

    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    try {
      objectMapper.writeValue(response.getWriter(), error);
    } catch (IOException e) {
      throw new ErroInternoException("Erro ao escrever a resposta de erro", e);
    }
  }
}
