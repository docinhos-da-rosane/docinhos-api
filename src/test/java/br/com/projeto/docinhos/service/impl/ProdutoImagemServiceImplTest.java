package br.com.projeto.docinhos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.config.ImagemProperties;
import br.com.projeto.docinhos.exception.ErroInternoException;
import br.com.projeto.docinhos.exception.RequisicaoInvalidaException;
import br.com.projeto.docinhos.integration.image.cloudinary.CloudinaryStorage;
import br.com.projeto.docinhos.integration.image.dto.ImagemArmazenadaResponse;
import br.com.projeto.docinhos.mocks.ProdutoMock;
import br.com.projeto.docinhos.model.ProdutoImagem;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ProdutoImagemServiceImplTest {

  @Mock private CloudinaryStorage cloudinaryStorage;

  @Mock private ImagemProperties imagemProperties;

  @InjectMocks private ProdutoImagemServiceImpl produtoImagemService;

  private static final String CAMINHO = "caminho/teste";
  private static final Set<String> TIPOS_DE_IMAGEM_PERMITIDOS = Set.of("image/jpeg", "image/png");

  @Nested
  class ArmazenarTests {

    @Test
    void deveArmazenarImagemComSucesso() throws IOException {
      MockMultipartFile arquivo = ProdutoMock.criaMultiPartImagemPadrao();
      ImagemArmazenadaResponse imagemArmazenada = criarImagemArmazenadaResponsePadrao();

      when(imagemProperties.caminho()).thenReturn(CAMINHO);
      when(imagemProperties.tiposPermitidos()).thenReturn(TIPOS_DE_IMAGEM_PERMITIDOS);
      when(cloudinaryStorage.armazenar(arquivo.getBytes(), CAMINHO)).thenReturn(imagemArmazenada);

      ProdutoImagem produtoImagem = produtoImagemService.armazenar(arquivo);

      assertEquals(imagemArmazenada.url(), produtoImagem.getUrl());
      assertEquals(imagemArmazenada.idArmazenamento(), produtoImagem.getIdArmazenamento());
    }

    @Test
    void deveLancarRequisicaoInvalidaExceptionQuandoImagemForVazia() {
      MockMultipartFile arquivo = new MockMultipartFile("arquivo", "", "image/jpeg", new byte[0]);

      assertThrows(
          RequisicaoInvalidaException.class, () -> produtoImagemService.armazenar(arquivo));
      verify(cloudinaryStorage, never()).armazenar(any(), any());
    }

    @Test
    void deveLancarRequisicaoInvalidaExceptionQuandoImagemForTipoInvalido() {
      MockMultipartFile arquivo =
          new MockMultipartFile("arquivo", "imagem.jpg", MediaType.IMAGE_GIF_VALUE, new byte[10]);

      assertThrows(
          RequisicaoInvalidaException.class, () -> produtoImagemService.armazenar(arquivo));
      verify(cloudinaryStorage, never()).armazenar(any(), any());
    }

    @Test
    void deveLancarErroInternoExceptionQuandoFalharAoArmazenarImagem() throws IOException {
      MockMultipartFile arquivo =
          new MockMultipartFile("arquivo", "imagem.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[10]);

      MockMultipartFile arquivoSpy = spy(arquivo);

      when(imagemProperties.tiposPermitidos()).thenReturn(TIPOS_DE_IMAGEM_PERMITIDOS);
      doThrow(new IOException("Falha ao ler bytes da imagem")).when(arquivoSpy).getBytes();

      assertThrows(ErroInternoException.class, () -> produtoImagemService.armazenar(arquivoSpy));
    }
  }

  @Nested
  class RollbackTests {
    @Test
    void deveExecutarRollbackComSucesso() {
      ProdutoImagem produtoImagem = ProdutoMock.criarProdutoImagemPadrao();

      produtoImagemService.rollback(produtoImagem);

      verify(cloudinaryStorage, times(1)).deletar("idArmazenamento123");
    }

    @Test
    void deveIgnorarRollbackQuandoProdutoImagemForNulo() {
      produtoImagemService.rollback(null);
      verify(cloudinaryStorage, never()).deletar(any());
    }

    @Test
    void deveLancarErroInternoExceptionQuandoFalharAoExecutarRollback() {
      ProdutoImagem produtoImagem = ProdutoMock.criarProdutoImagemPadrao();

      doThrow(new ErroInternoException("Falha ao deletar imagem", new RuntimeException()))
          .when(cloudinaryStorage)
          .deletar(produtoImagem.getIdArmazenamento());

      assertThrows(ErroInternoException.class, () -> produtoImagemService.rollback(produtoImagem));
    }
  }

  @Nested
  class DeletarTests {

    @Test
    void deveDeletarImagemComSucesso() {
      ProdutoImagem produtoImagem = ProdutoMock.criarProdutoImagemPadrao();

      produtoImagemService.deletar(produtoImagem);

      verify(cloudinaryStorage, times(1)).deletar(produtoImagem.getIdArmazenamento());
    }

    @Test
    void deveLancarErroInternoExceptionQuandoFalharAoDeletarImagem() {
      ProdutoImagem produtoImagem = ProdutoMock.criarProdutoImagemPadrao();

      doThrow(new ErroInternoException("Falha ao deletar imagem", new RuntimeException()))
          .when(cloudinaryStorage)
          .deletar(produtoImagem.getIdArmazenamento());

      assertThrows(ErroInternoException.class, () -> produtoImagemService.deletar(produtoImagem));
    }
  }

  private ImagemArmazenadaResponse criarImagemArmazenadaResponsePadrao() {
    return new ImagemArmazenadaResponse("idArmazenamento123", "https://example.com/imagem.jpg");
  }
}
