package br.com.projeto.docinhos.modules.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProdutoDisponibilidadeRequest(
    @Schema(description = "Indica se o produto deve ficar disponível ou não")
        @NotNull(message = "O campo 'disponivel' é obrigatório")
        Boolean disponivel) {}
