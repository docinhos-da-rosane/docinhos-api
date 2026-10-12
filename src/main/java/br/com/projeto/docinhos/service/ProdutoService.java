package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.PageResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResumoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProdutoService {
  ProdutoResponse criarProduto(MultipartFile imagem, CriarProdutoRequest request);

  PageResponse<ProdutoResumoResponse> listarProdutos(int page, int size);
}
