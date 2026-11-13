package br.com.projeto.docinhos.modules.admin.service;

import br.com.projeto.docinhos.modules.admin.dto.request.ProdutoDisponibilidadeRequest;
import br.com.projeto.docinhos.modules.admin.dto.request.ProdutoRequest;
import br.com.projeto.docinhos.modules.admin.dto.response.ProdutoDetalhadoResponse;
import br.com.projeto.docinhos.modules.admin.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.modules.admin.dto.response.ProdutoResumoResponse;
import br.com.projeto.docinhos.shared.dto.PageResponse;
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
