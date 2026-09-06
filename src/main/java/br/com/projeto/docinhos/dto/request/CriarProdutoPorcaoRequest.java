package br.com.projeto.docinhos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record CriarProdutoPorcaoRequest(
    @Schema(description = "Quantidade de unidades da porção", example = "15")
        @NotNull(message = "O campo 'quantidade' é obrigatório")
        @Positive(message = "O campo 'quantidade' deve ser maior que zero")
        Integer quantidade,
    @Schema(description = "Preço da porção", example = "55.00")
        @NotNull(message = "O campo 'preço' é obrigatório")
        @Positive(message = "O campo 'preço' deve ser maior que zero")
        BigDecimal preco) {}
