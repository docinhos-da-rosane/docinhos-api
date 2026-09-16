package br.com.projeto.docinhos.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProdutoResponse(
    @Schema(
            description = "Identificador do produto",
            example = "505605a1-1536-43d2-bb92-56e5f354652f")
        UUID id,
    @Schema(description = "Nome do produto", example = "Brigadeiro") String nome,
    @Schema(
            description = "Descrição do produto",
            example = "Brigadeiro tradicional com chocolate e granulado.")
        String descricao,
    @Schema(description = "Categoria do produto") CategoriaResponse categoria,
    @Schema(description = "URL da imagem do produto", example = "https://example.com/imagem.jpg")
        String imagemUrl,
    @Schema(description = "Porções disponíveis para o produto") List<ProdutoPorcaoResponse> porcoes,
    @Schema(description = "Indica se o produto está disponível para encomenda", example = "true")
        boolean disponivel,
    @Schema(description = "Data e hora de criação do produto", example = "2026-09-06T12:30:00Z")
        Instant dataCriacao,
    @Schema(
            description = "Data e hora da última atualização do produto",
            example = "2026-09-06T12:30:00Z")
        Instant dataAtualizacao) {}
