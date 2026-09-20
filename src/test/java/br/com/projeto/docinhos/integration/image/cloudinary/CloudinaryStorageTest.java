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
        Map.of(
            "public_id", "public-id-123",
            "secure_url", "https://cloudinary.com/imagem.jpg");

    when(uploader.upload(eq(imagem), any(Map.class))).thenReturn(uploadResultado);

    ImagemArmazenadaResponse response = cloudinaryStorage.armazenar(imagem, caminho);

    assertEquals("public-id-123", response.idArmazenamento());
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

  @Test
  void deveDeletarImagemComSucesso() throws Exception {
    cloudinaryStorage.deletar("public-id-123");

    verify(uploader).destroy(eq("public-id-123"), anyMap());
  }

  @Test
  void deveLancarErroInternoExceptionQuandoFalharAoDeletar() throws Exception {
    doThrow(new RuntimeException("falha ao deletar")).when(uploader).destroy(anyString(), anyMap());

    ErroInternoException exception =
        assertThrows(ErroInternoException.class, () -> cloudinaryStorage.deletar("public-id-123"));

    assertTrue(exception.getMessage().contains("Erro ao deletar a imagem do Cloudinary"));
  }
}
