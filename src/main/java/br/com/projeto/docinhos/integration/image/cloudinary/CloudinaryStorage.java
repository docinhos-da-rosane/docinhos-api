package br.com.projeto.docinhos.integration.image.cloudinary;

import br.com.projeto.docinhos.exception.ErroInternoException;
import br.com.projeto.docinhos.integration.image.ImagemStorage;
import br.com.projeto.docinhos.integration.image.dto.ImagemArmazenadaResponse;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
  public String gerarUrl(String idArmazenamento, int dimensao) {
    return gerarUrl(idArmazenamento, dimensao, false);
  }

  @Override
  public String gerarUrl(String idArmazenamento, int dimensao, boolean recortar) {
    return cloudinary
        .url()
        .transformation(criarTransformacao(dimensao, recortar))
        .secure(true)
        .generate(idArmazenamento);
  }

  @Override
  public void deletar(String idArmazenamento) {
    log.info("Iniciando exclusão de imagem no Cloudinary. idArmazenamento={}", idArmazenamento);

    try {
      Map<?, ?> resultado = cloudinary.uploader().destroy(idArmazenamento, ObjectUtils.emptyMap());

      if (validarExclusao(resultado)) {
        throw new IllegalStateException(
            "Cloudinary não confirmou a exclusão da imagem: " + idArmazenamento);
      }

      log.info("Imagem excluída com sucesso do Cloudinary. idArmazenamento={}", idArmazenamento);

    } catch (Exception ex) {
      throw new ErroInternoException(
          "Erro ao deletar a imagem do Cloudinary: " + ex.getMessage(), ex);
    }
  }

  @Override
  public void restaurar(String idArmazenamento) {
    log.info("Iniciando restauração de imagem no Cloudinary. idArmazenamento={}", idArmazenamento);

    try {
      ApiResponse resultado =
          cloudinary
              .api()
              .restore(
                  List.of(idArmazenamento),
                  ObjectUtils.asMap("resource_type", "image", "type", "upload"));

      if (validarRestauracao(resultado, idArmazenamento)) {
        throw new IllegalStateException(
            "Cloudinary não confirmou a restauração da imagem: " + idArmazenamento);
      }

      log.info("Imagem restaurada com sucesso no Cloudinary. idArmazenamento={}", idArmazenamento);

    } catch (Exception ex) {
      throw new ErroInternoException(
          "Erro ao restaurar a imagem do Cloudinary: " + ex.getMessage(), ex);
    }
  }

  private boolean validarRestauracao(Map<?, ?> resultado, String idArmazenamento) {
    Object imagemRestaurada = Objects.isNull(resultado) ? null : resultado.get(idArmazenamento);

    return !(imagemRestaurada instanceof Map<?, ?> imagem)
        || !Objects.equals(idArmazenamento, imagem.get("public_id"));
  }

  private boolean validarExclusao(Map<?, ?> resultado) {
    Object status = Objects.isNull(resultado) ? null : resultado.get("result");
    return !Objects.equals("ok", status) && !Objects.equals("not found", status);
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

  private Transformation<?> criarTransformacao(int dimensao, boolean recortar) {
    Transformation<?> transformation = new Transformation<>().width(dimensao).height(dimensao);

    return recortar ? transformation.gravity("center").crop("lfill") : transformation.crop("limit");
  }
}
