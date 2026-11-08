package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.dto.request.ProdutoDisponibilidadeRequest;
import br.com.projeto.docinhos.dto.request.ProdutoRequest;
import br.com.projeto.docinhos.dto.response.PageResponse;
import br.com.projeto.docinhos.dto.response.ProdutoDetalhadoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResumoResponse;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface ProdutoService {
  ProdutoResponse criarProduto(MultipartFile imagem, ProdutoRequest request);

  PageResponse<ProdutoResumoResponse> listarProdutos(int page, int size);

  ProdutoDetalhadoResponse buscarProduto(UUID id);

  void atualizarProduto(UUID id, MultipartFile imagem, ProdutoRequest request);

  void alterarDisponibilidade(UUID id, ProdutoDisponibilidadeRequest request);

  void deletarProduto(UUID id);
}
