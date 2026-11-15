package br.com.projeto.docinhos.modules.publico.controller.impl;

import br.com.projeto.docinhos.modules.publico.controller.ProdutoPublicoController;
import br.com.projeto.docinhos.modules.publico.dto.response.ProdutoResumoPublicoResponse;
import br.com.projeto.docinhos.modules.publico.service.ProdutoPublicoService;
import br.com.projeto.docinhos.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProdutoPublicoControllerImpl implements ProdutoPublicoController {

  private final ProdutoPublicoService produtoPublicoService;

  @Override
  @GetMapping
  public PageResponse<ProdutoResumoPublicoResponse> listarProdutos(int page, int size) {
    return produtoPublicoService.listarProdutos(page, size);
  }
}
