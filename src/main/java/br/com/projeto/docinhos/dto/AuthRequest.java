package br.com.projeto.docinhos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AuthRequest(

  @NotBlank(message = "O campo email é obrigatório")
  @Email(message = "O campo email deve ser válido")
  @Size(max = 100, message = "O campo email deve ter no máximo 100 caracteres")
  @Schema(description = "Email do usuário", example = "docinho@gmail.com")
  String email,

  @NotBlank(message = "O campo senha é obrigatório")
  @Size(max = 100, message = "O campo senha deve ter no máximo 100 caracteres")
  @Schema(description = "Senha do usuário", example = "123456")
  String senha
) {
}
