package br.com.projeto.docinhos.integration.image;

import br.com.projeto.docinhos.integration.image.dto.ImagemArmazenadaResponse;

public interface ImagemStorage {
  ImagemArmazenadaResponse armazenar(byte[] imagem, String caminho);

  String gerarUrl(String idArmazenamento, int dimensao);

  String gerarUrl(String idArmazenamento, int dimensao, boolean recortar);

  void deletar(String idArmazenamento);
}
