package br.com.projeto.docinhos.modules.publico.service.impl;

import br.com.projeto.docinhos.domain.model.ProdutoImagem;
import br.com.projeto.docinhos.integration.image.ImagemStorage;
import br.com.projeto.docinhos.modules.publico.service.ProdutoImagemPublicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoImagemPublicoServiceImpl implements ProdutoImagemPublicoService {

  private static final int LARGURA_MINIATURA = 800;
  private static final int ALTURA_MINIATURA = 450;

  private final ImagemStorage imagemStorage;

  @Override
  public String gerarUrlMenor(ProdutoImagem produtoImagem) {
    return imagemStorage.gerarUrl(
        produtoImagem.getIdArmazenamento(), LARGURA_MINIATURA, ALTURA_MINIATURA, true);
  }
}
