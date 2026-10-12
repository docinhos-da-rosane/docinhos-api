package br.com.projeto.docinhos.integration.image;

import br.com.projeto.docinhos.integration.image.dto.ImagemArmazenadaResponse;

public interface ImagemStorage {
  ImagemArmazenadaResponse armazenar(byte[] imagem, String caminho);

  String gerarUrlMiniatura(String idArmazenamento);

  void deletar(String idArmazenamento);
}
