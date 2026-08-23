package br.com.projeto.docinhos.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.docinhos.exception.NaoEncontradoException;
import br.com.projeto.docinhos.model.Usuario;
import br.com.projeto.docinhos.repository.UsuarioRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceImplTest {

  @Mock private UsuarioRepository usuarioRepository;

  @InjectMocks private UsuarioDetailsServiceImpl usuarioDetailsService;

  @Nested
  class LoadUserByUsernameTests {

    @Test
    void deveRetornarSecurityUsuarioQuandoUsuarioExiste() {
      String email = "maria@email.com";
      String senha = "senha123";

      Usuario usuario = new Usuario(UUID.randomUUID(), email, senha);
      when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

      SecurityUsuario resultado = usuarioDetailsService.loadUserByUsername(email);

      assertEquals(email, resultado.getUsername());
      assertEquals(senha, resultado.getPassword());
      verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void deveLancarNaoEncontradoExceptionQuandoUsuarioNaoExiste() {
      String mensagemEsperada = "Usuário não encontrado";
      String emailInexistente = "inexistente@docinhos.com";

      when(usuarioRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

      NaoEncontradoException exception =
          assertThrows(
              NaoEncontradoException.class,
              () -> usuarioDetailsService.loadUserByUsername(emailInexistente));

      assertEquals(mensagemEsperada, exception.getMessage());
      assertInstanceOf(NaoEncontradoException.class, exception);
      verify(usuarioRepository).findByEmail(emailInexistente);
    }
  }
}
