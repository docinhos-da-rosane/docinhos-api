package br.com.projeto.docinhos.controller;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ErrorResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
@RequestMapping("/produtos")
public interface ProdutoController {

  @Operation(
      summary = "Cadastrar produto",
      description = "Cadastra um novo produto e suas respectivas porções.")
  @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso")
  @ApiResponse(
      responseCode = "400",
      description = "Dados informados para o cadastro são inválidos",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Categoria informada não encontrada",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Erro interno ao cadastrar o produto",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  ProdutoResponse cadastrarProduto(CriarProdutoRequest request);
}
