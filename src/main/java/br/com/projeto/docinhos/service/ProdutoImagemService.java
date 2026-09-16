package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.model.ProdutoImagem;
import org.springframework.web.multipart.MultipartFile;

public interface ProdutoImagemService {
  ProdutoImagem armazenar(MultipartFile arquivo);

  void deletar(ProdutoImagem produtoImagem);

  void rollback(ProdutoImagem produtoImagem);
}
