package br.com.projeto.docinhos.controller;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ErrorResponse;
import br.com.projeto.docinhos.dto.response.PageResponse;
import br.com.projeto.docinhos.dto.response.ProdutoDetalhadoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResumoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
@RequestMapping("/produtos")
public interface ProdutoController {

  @Operation(
      summary = "Consultar detalhes do produto",
      description = "Retorna os detalhes de um produto, incluindo categoria, fotografia e porções.")
  @ApiResponse(
      responseCode = "200",
      description = "Detalhes do produto retornados com sucesso",
      content = @Content(schema = @Schema(implementation = ProdutoDetalhadoResponse.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Identificador inválido",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Produto não encontrado",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Falha inesperada ao consultar o produto",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  ProdutoDetalhadoResponse buscarProduto(
      @Parameter(
              description = "UUID do produto",
              required = true,
              example = "8e12b02d-35f0-4d84-8a1c-03fd4d979e65")
          @PathVariable("id")
          UUID id);

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
  ProdutoResponse cadastrarProduto(MultipartFile imagem, CriarProdutoRequest request);

  @Operation(summary = "Listar produtos", description = "Retorna a lista paginada de produtos.")
  @ApiResponse(
      responseCode = "200",
      description = "Lista de produtos retornada com sucesso",
      content = @Content(schema = @Schema(implementation = PageResponse.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Erro interno ao consultar os produtos",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  PageResponse<ProdutoResumoResponse> listarProdutos(
      @Parameter(description = "Índice da página iniciado em zero", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Quantidade de itens por página", example = "6")
          @RequestParam(defaultValue = "6")
          int size);
}
