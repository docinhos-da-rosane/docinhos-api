package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProdutoService {
  ProdutoResponse criarProduto(MultipartFile imagem, CriarProdutoRequest request);
}
