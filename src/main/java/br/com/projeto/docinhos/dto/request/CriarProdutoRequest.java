package br.com.projeto.docinhos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record CriarProdutoRequest(
    @Schema(description = "Nome do produto", example = "Brigadeiro")
        @NotBlank(message = "O campo 'nome' é obrigatório")
        @Size(max = 100, message = "O campo 'nome' deve possuir no máximo 100 caracteres")
        String nome,
    @Schema(
            description = "Identificador da categoria do produto",
            example = "505605a1-1536-43d2-bb92-56e5f354652f")
        @NotNull(message = "O campo 'categoria' é obrigatório")
        UUID categoriaId,
    @Schema(
            description = "Descrição do produto",
            example = "Brigadeiro tradicional com chocolate e granulado.")
        @NotBlank(message = "O campo 'descrição' é obrigatório")
        @Size(max = 500, message = "O campo 'descrição' deve possuir no máximo 500 caracteres")
        String descricao,
    @Schema(description = "Porções disponíveis para o produto")
        @NotNull(message = "O campo 'porções' é obrigatório")
        @NotEmpty(message = "O produto deve possuir pelo menos uma porção")
        List<@Valid CriarProdutoPorcaoRequest> porcoes) {}
