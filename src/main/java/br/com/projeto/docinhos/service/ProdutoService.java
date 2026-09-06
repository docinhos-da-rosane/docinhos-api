package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;

public interface ProdutoService {
  ProdutoResponse criarProduto(CriarProdutoRequest request);
}
