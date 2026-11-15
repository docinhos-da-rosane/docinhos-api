package br.com.projeto.docinhos.modules.publico.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder()
public record ProdutoResumoPublicoResponse(
    @Schema(
            description = "Identificador do produto",
            example = "505605a1-1536-43d2-bb92-56e5f354652f")
        UUID id,
    @Schema(description = "Nome do produto", example = "Brigadeiro") String nome,
    @Schema(description = "Nome da categoria", example = "Tradicional") String categoria,
    @Schema(
            description = "URL da imagem menor pública",
            example =
                "https://res.cloudinary.com/demo/image/upload/c_lfill,h_450,w_800/produto.jpg")
        String imagemMenorUrl,
    @Schema(description = "Indica se o produto está disponível", example = "true")
        boolean disponivel,
    @Schema(description = "Porção com o menor preço")
        ProdutoPorcaoPublicoResponse porcaoMenorPreco) {}
