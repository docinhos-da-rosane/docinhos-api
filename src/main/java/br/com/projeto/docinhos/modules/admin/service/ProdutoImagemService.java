package br.com.projeto.docinhos.modules.admin.service;

import br.com.projeto.docinhos.domain.model.Produto;
import br.com.projeto.docinhos.domain.model.ProdutoImagem;
import org.springframework.web.multipart.MultipartFile;

public interface ProdutoImagemService {
  ProdutoImagem armazenar(MultipartFile arquivo);

  String gerarUrlMiniatura(ProdutoImagem produtoImagem);

  String gerarUrlMedia(ProdutoImagem produtoImagem);

  void deletar(ProdutoImagem produtoImagem);

  void restaurar(String idArmazenamento);

  void rollback(ProdutoImagem produtoImagem);

  void rollback(
      ProdutoImagem produtoImagemNova, ProdutoImagem produtoImagemAnterior, Produto produto);
}
