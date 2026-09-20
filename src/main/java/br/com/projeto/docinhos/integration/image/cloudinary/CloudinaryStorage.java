package br.com.projeto.docinhos.integration.image.cloudinary;

import br.com.projeto.docinhos.exception.ErroInternoException;
import br.com.projeto.docinhos.integration.image.ImagemStorage;
import br.com.projeto.docinhos.integration.image.dto.ImagemArmazenadaResponse;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudinaryStorage implements ImagemStorage {

  private static final int DIMENSAO_MAXIMA_IMAGEM = 1600;
  private static final String FORMATO_IMAGEM = "jpg";
  private static final String QUALIDADE_IMAGEM = "auto";

  private final Cloudinary cloudinary;

  @Override
  public ImagemArmazenadaResponse armazenar(byte[] imagem, String caminho) {
    log.info("Iniciando armazenamento de imagem no Cloudinary");

    try {
      Map<String, Object> uploadParametros = criarParametrosUpload(caminho);
      Map<?, ?> uploadResultado = cloudinary.uploader().upload(imagem, uploadParametros);

      String publicId = (String) uploadResultado.get("public_id");
      String url = (String) uploadResultado.get("secure_url");

      log.info("Imagem armazenada com sucesso no Cloudinary. idArmazenamento={}", publicId);

      return new ImagemArmazenadaResponse(publicId, url);

    } catch (Exception ex) {
      throw new ErroInternoException(
          "Erro ao armazenar a imagem no Cloudinary: " + ex.getMessage(), ex);
    }
  }

  @Override
  public void deletar(String idArmazenamento) {
    log.info("Iniciando exclusão de imagem no Cloudinary. idArmazenamento={}", idArmazenamento);

    try {
      cloudinary.uploader().destroy(idArmazenamento, ObjectUtils.emptyMap());
      log.info("Imagem excluída com sucesso do Cloudinary. idArmazenamento={}", idArmazenamento);

    } catch (Exception ex) {
      throw new ErroInternoException(
          "Erro ao deletar a imagem do Cloudinary: " + ex.getMessage(), ex);
    }
  }

  private Map<String, Object> criarParametrosUpload(String caminho) {
    Map<String, Object> parametros = new HashMap<>();

    parametros.put("asset_folder", caminho);
    parametros.put("transformation", criarOtimizacao());

    return parametros;
  }

  private Transformation<?> criarOtimizacao() {
    return new Transformation<>()
        .width(DIMENSAO_MAXIMA_IMAGEM)
        .height(DIMENSAO_MAXIMA_IMAGEM)
        .crop("limit")
        .fetchFormat(FORMATO_IMAGEM)
        .quality(QUALIDADE_IMAGEM);
  }
}
