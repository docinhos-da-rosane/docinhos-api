package br.com.projeto.docinhos.controller.impl;

import br.com.projeto.docinhos.controller.ProdutoController;
import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProdutoControllerImpl implements ProdutoController {

  private final ProdutoService produtoService;

  @Override
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ProdutoResponse cadastrarProduto(
      @Valid @RequestPart("produto") CriarProdutoRequest request) {
    return produtoService.criarProduto(request);
  }
}
