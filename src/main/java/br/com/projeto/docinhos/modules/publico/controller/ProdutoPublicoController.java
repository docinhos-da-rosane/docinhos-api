package br.com.projeto.docinhos.modules.publico.controller;

import br.com.projeto.docinhos.modules.publico.dto.response.ProdutoResumoPublicoResponse;
import br.com.projeto.docinhos.shared.dto.ErrorResponse;
import br.com.projeto.docinhos.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Catálogo público", description = "Consulta pública de produtos")
@RequestMapping("/publico/produtos")
public interface ProdutoPublicoController {

  @Operation(
      summary = "Listar produtos do catálogo público",
      description = "Retorna produtos em ordem de disponibilidade e nome.")
  @ApiResponse(
      responseCode = "200",
      description = "Página de produtos retornada com sucesso",
      content = @Content(schema = @Schema(implementation = PageResponse.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Falha ao carregar os produtos",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  PageResponse<ProdutoResumoPublicoResponse> listarProdutos(
      @Parameter(description = "Índice da página iniciado em zero", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Quantidade de itens por página", example = "6")
          @RequestParam(defaultValue = "6")
          int size);
}
