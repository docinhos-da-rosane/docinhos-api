package br.com.projeto.docinhos.service.impl;

import static java.util.Objects.isNull;

import br.com.projeto.docinhos.config.ImagemProperties;
import br.com.projeto.docinhos.exception.ErroInternoException;
import br.com.projeto.docinhos.exception.RequisicaoInvalidaException;
import br.com.projeto.docinhos.integration.image.ImagemStorage;
import br.com.projeto.docinhos.integration.image.dto.ImagemArmazenadaResponse;
import br.com.projeto.docinhos.model.ProdutoImagem;
import br.com.projeto.docinhos.service.ProdutoImagemService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoImagemServiceImpl implements ProdutoImagemService {

  private final ImagemStorage imagemStorage;
  private final ImagemProperties imagemProperties;

  @Override
  public ProdutoImagem armazenar(MultipartFile arquivo) {
    log.info("Armazenando imagem do produto. nomeOriginal={}", arquivo.getOriginalFilename());

    try {
      validarImagem(arquivo);
      ImagemArmazenadaResponse imagemArmazenada =
          imagemStorage.armazenar(arquivo.getBytes(), imagemProperties.caminho());

      log.info(
          "Imagem do produto armazenada com sucesso. idArmazenamento={}",
          imagemArmazenada.idArmazenamento());

      return new ProdutoImagem(imagemArmazenada.url(), imagemArmazenada.idArmazenamento());

    } catch (IOException ex) {
      throw new ErroInternoException("Não foi possível ler a imagem", ex);
    }
  }

  @Override
  public void rollback(ProdutoImagem produtoImagem) {
    if (isNull(produtoImagem)) {
      return;
    }

    log.info(
        "Rollback: removendo imagem do produto. idArmazenamento={}",
        produtoImagem.getIdArmazenamento());

    try {
      deletar(produtoImagem);
    } catch (Exception cleanupEx) {
      log.error(
          "Rollback: falha ao remover imagem do produto. idArmazenamento={}. Erro: {}",
          produtoImagem.getIdArmazenamento(),
          cleanupEx.getMessage());
      throw new ErroInternoException("Falha ao executar rollback da imagem do produto", cleanupEx);
    }

    log.info(
        "Rollback: imagem do produto removida com sucesso. idArmazenamento={}",
        produtoImagem.getIdArmazenamento());
  }

  @Override
  public void deletar(ProdutoImagem produtoImagem) {
    log.info("Deletando imagem do produto. idArmazenamento={}", produtoImagem.getIdArmazenamento());

    imagemStorage.deletar(produtoImagem.getIdArmazenamento());

    log.info(
        "Imagem do produto deletada com sucesso. idArmazenamento={}",
        produtoImagem.getIdArmazenamento());
  }

  private void validarImagem(MultipartFile arquivo) {
    validarPresenca(arquivo);
    validarTipoArquivo(arquivo);
  }

  private void validarPresenca(MultipartFile arquivo) {
    if (isNull(arquivo) || arquivo.isEmpty()) {
      throw new RequisicaoInvalidaException("A imagem do produto é obrigatória");
    }
  }

  private void validarTipoArquivo(MultipartFile arquivo) {
    if (!imagemProperties.tiposPermitidos().contains(arquivo.getContentType())) {
      throw new RequisicaoInvalidaException(
          "A imagem do produto deve estar no formato JPG, JPEG ou PNG");
    }
  }
}
