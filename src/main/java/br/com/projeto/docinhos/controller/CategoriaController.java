package br.com.projeto.docinhos.controller;

import br.com.projeto.docinhos.dto.CategoriaResponse;
import br.com.projeto.docinhos.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Categorias", description = "Operações para consulta de categorias de produtos")
@RequestMapping("/categorias")
public interface CategoriaController {

  @Operation(
      summary = "Buscar categorias",
      description = "Retorna as categorias de produtos cadastradas no sistema.")
  @ApiResponse(responseCode = "200", description = "Categorias retornadas com sucesso")
  @ApiResponse(
      responseCode = "500",
      description = "Erro interno ao buscar as categorias",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  @GetMapping
  List<CategoriaResponse> buscarCategorias();
}
