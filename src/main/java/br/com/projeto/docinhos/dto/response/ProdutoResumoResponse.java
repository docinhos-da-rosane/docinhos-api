package br.com.projeto.docinhos.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProdutoResumoResponse(
    @Schema(
            description = "Identificador do produto",
            example = "505605a1-1536-43d2-bb92-56e5f354652f")
        UUID id,
    @Schema(description = "Nome do produto", example = "Brigadeiro") String nome,
    @Schema(description = "Nome da categoria", example = "Tradicional") String categoria,
    @Schema(
            description = "URL da miniatura da fotografia do produto",
            example =
                "https://res.cloudinary.com/demo/image/upload/c_lfill,h_128,w_128/produto.jpg")
        String imagemMiniaturaUrl,
    @Schema(description = "Indica se o produto está disponível", example = "true")
        boolean disponivel) {}
