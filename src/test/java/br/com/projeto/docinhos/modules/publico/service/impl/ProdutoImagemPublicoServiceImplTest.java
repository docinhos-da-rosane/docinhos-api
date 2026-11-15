package br.com.projeto.docinhos.modules.publico.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.domain.model.ProdutoImagem;
import br.com.projeto.docinhos.integration.image.ImagemStorage;
import br.com.projeto.docinhos.shared.mocks.ProdutoMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProdutoImagemPublicoServiceImplTest {

  @Mock private ImagemStorage imagemStorage;

  @InjectMocks private ProdutoImagemPublicoServiceImpl service;

  @Test
  void deveDelegarGeracaoDaUrlImagemMenor() {
    ProdutoImagem imagem = ProdutoMock.criarProdutoImagemPadrao();
    String urlMenor = "https://example.com/800x450.jpg";

    when(imagemStorage.gerarUrl(imagem.getIdArmazenamento(), 800, 450, true)).thenReturn(urlMenor);

    String url = service.gerarUrlMenor(imagem);

    assertEquals(urlMenor, url);
    verify(imagemStorage).gerarUrl(imagem.getIdArmazenamento(), 800, 450, true);
  }
}
