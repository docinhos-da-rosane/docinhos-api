package br.com.projeto.docinhos.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PageResponse<T>(
    @Schema(description = "Itens da página atual") List<T> content,
    @Schema(description = "Índice da página", example = "0") int page,
    @Schema(description = "Quantidade de itens por página", example = "6") int size,
    @Schema(description = "Quantidade total de itens", example = "12") long totalElements,
    @Schema(description = "Quantidade total de páginas", example = "2") int totalPages) {}
