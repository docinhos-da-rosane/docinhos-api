package br.com.projeto.docinhos.integration.image;

import br.com.projeto.docinhos.integration.image.dto.ImagemArmazenadaResponse;

public interface ImageStorage {
  ImagemArmazenadaResponse armazenar(byte[] arquivo, String caminho);

  void deletar(String idArmazenamento);
}
