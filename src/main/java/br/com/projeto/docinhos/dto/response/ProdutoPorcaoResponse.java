package br.com.projeto.docinhos.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProdutoPorcaoResponse(
    @Schema(
            description = "Identificador da porção do produto",
            example = "505605a1-1536-43d2-bb92-56e5f354652f")
        UUID id,
    @Schema(description = "Quantidade de unidades da porção", example = "15") int quantidade,
    @Schema(description = "Preço da porção do produto", example = "55.00") BigDecimal preco) {}
