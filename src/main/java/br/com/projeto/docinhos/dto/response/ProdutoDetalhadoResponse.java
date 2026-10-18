package br.com.projeto.docinhos.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProdutoDetalhadoResponse(
    @Schema(
            description = "Identificador do produto",
            example = "8e12b02d-35f0-4d84-8a1c-03fd4d979e65")
        UUID id,
    @Schema(description = "Nome do produto", example = "Brigadeiro Tradicional") String nome,
    @Schema(description = "Descrição completa do produto") String descricao,
    @Schema(description = "Nome da categoria do produto", example = "Tradicionais")
        String categoria,
    @Schema(description = "Indica se o produto está disponível", example = "true")
        boolean disponivel,
    @Schema(description = "URL da imagem limitada a 1200 por 1200 pixels") String imagemUrl,
    @Schema(description = "Porções do produto ordenadas por quantidade crescente")
        List<ProdutoPorcaoResponse> porcoes) {}
