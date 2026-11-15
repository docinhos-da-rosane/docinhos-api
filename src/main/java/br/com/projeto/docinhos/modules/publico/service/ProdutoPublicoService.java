package br.com.projeto.docinhos.modules.publico.service;

import br.com.projeto.docinhos.modules.publico.dto.response.ProdutoResumoPublicoResponse;
import br.com.projeto.docinhos.shared.dto.PageResponse;

public interface ProdutoPublicoService {

  PageResponse<ProdutoResumoPublicoResponse> listarProdutos(int page, int size);
}
