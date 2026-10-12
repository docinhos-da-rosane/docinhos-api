package br.com.projeto.docinhos.integration.image.cloudinary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.exception.ErroInternoException;
import br.com.projeto.docinhos.integration.image.dto.ImagemArmazenadaResponse;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CloudinaryStorageTest {

  @Mock private Cloudinary cloudinary;

  @Mock private Uploader uploader;

  private CloudinaryStorage cloudinaryStorage;

  private static final String ID_ARMAZENAMENTO = "public-id-123";

  @Nested
  class ArmazenarTests {

    @BeforeEach
    void setUp() {
      cloudinaryStorage = new CloudinaryStorage(cloudinary);
      when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void deveArmazenarImagemComSucesso() throws Exception {
      byte[] imagem = "imagem".getBytes();
      String caminho = "produtos";
      Map<String, Object> uploadResultado =
          Map.of("public_id", ID_ARMAZENAMENTO, "secure_url", "https://cloudinary.com/imagem.jpg");

      when(uploader.upload(eq(imagem), any(Map.class))).thenReturn(uploadResultado);

      ImagemArmazenadaResponse response = cloudinaryStorage.armazenar(imagem, caminho);

      assertEquals(ID_ARMAZENAMENTO, response.idArmazenamento());
      assertEquals("https://cloudinary.com/imagem.jpg", response.url());

      ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
      verify(uploader).upload(eq(imagem), captor.capture());

      assertEquals(caminho, captor.getValue().get("asset_folder"));
      assertNotNull(captor.getValue().get("transformation"));
    }

    @Test
    void deveLancarErroInternoExceptionQuandoFalharAoArmazenar() throws Exception {
      byte[] imagem = "imagem".getBytes();

      when(uploader.upload(eq(imagem), any(Map.class)))
          .thenThrow(new RuntimeException("falha ao enviar para o Cloudinary"));

      ErroInternoException exception =
          assertThrows(
              ErroInternoException.class, () -> cloudinaryStorage.armazenar(imagem, "produtos"));

      assertTrue(exception.getMessage().contains("Erro ao armazenar a imagem no Cloudinary"));
    }
  }

  @Nested
  class DeletarTests {

    @BeforeEach
    void setUp() {
      cloudinaryStorage = new CloudinaryStorage(cloudinary);
      when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void deveDeletarImagemComSucesso() throws Exception {
      cloudinaryStorage.deletar(ID_ARMAZENAMENTO);

      verify(uploader).destroy(eq(ID_ARMAZENAMENTO), anyMap());
    }

    @Test
    void deveLancarErroInternoExceptionQuandoFalharAoDeletar() throws Exception {
      doThrow(new RuntimeException("falha ao deletar"))
          .when(uploader)
          .destroy(anyString(), anyMap());

      ErroInternoException exception =
          assertThrows(
              ErroInternoException.class, () -> cloudinaryStorage.deletar(ID_ARMAZENAMENTO));

      assertTrue(exception.getMessage().contains("Erro ao deletar a imagem do Cloudinary"));
    }
  }

  @Nested
  class GerarUrlMiniaturaTests {

    @Test
    void deveGerarUrlMiniaturaComRecorteCentralDeAte128Pixels() {
      Cloudinary cloudinaryConfigurado = new Cloudinary(Map.of("cloud_name", "demo"));
      CloudinaryStorage storage = new CloudinaryStorage(cloudinaryConfigurado);

      String url = storage.gerarUrlMiniatura(ID_ARMAZENAMENTO);

      assertTrue(url.startsWith("https://res.cloudinary.com/demo/image/upload/"));
      assertTrue(url.contains("c_lfill"));
      assertTrue(url.contains("w_128"));
      assertTrue(url.contains("h_128"));
      assertTrue(url.contains("g_center"));
    }
  }
}
