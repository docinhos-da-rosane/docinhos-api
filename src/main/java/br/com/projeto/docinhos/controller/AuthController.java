package br.com.projeto.docinhos.controller;

import br.com.projeto.docinhos.dto.AuthRequest;
import br.com.projeto.docinhos.dto.AuthResponse;
import br.com.projeto.docinhos.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Autenticação", description = "Operações para autenticação de usuários")
@RequestMapping("/auth")
public interface AuthController {

  @Operation(summary = "Autenticar usuário", description = "Realiza a autenticação do usuário e retorna um token JWT.")
  @ApiResponse(
    responseCode = "200",
    description = "Autenticação realizada com sucesso",
    content = @Content(schema = @Schema(implementation = AuthResponse.class))
  )
  @ApiResponse(
    responseCode = "400",
    description = "Requisição inválida",
    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
  )
  @ApiResponse(
    responseCode = "401",
    description = "Credenciais inválidas",
    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
  )
  @PostMapping("/login")
  AuthResponse logar(@Valid @RequestBody AuthRequest request);
}
