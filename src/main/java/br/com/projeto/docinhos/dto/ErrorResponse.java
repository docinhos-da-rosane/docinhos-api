package br.com.projeto.docinhos.dto;

import br.com.projeto.docinhos.enums.CodeError;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record ErrorResponse(
  @Schema(description = "Timestamp do erro", example = "2023-05-01T12:34:56.789Z")
  Instant timestamp,

  @Schema(description = "Código do erro", example = "CREDENCIAIS_INVALIDAS")
  CodeError codeError,

  @Schema(description = "Mensagem de erro detalhada", example = "As credenciais fornecidas são inválidas.")
  String mensagem,

  @Schema(description = "Caminho da requisição que gerou o erro", example = "/auth/login")
  String path
) {
}
