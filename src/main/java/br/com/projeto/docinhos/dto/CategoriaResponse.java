package br.com.projeto.docinhos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CategoriaResponse(
    @Schema(
            description = "Identificador da categoria",
            example = "505605a1-1536-43d2-bb92-56e5f354652f")
        UUID id,
    @Schema(description = "Nome da categoria", example = "Tradicional") String nome) {}
