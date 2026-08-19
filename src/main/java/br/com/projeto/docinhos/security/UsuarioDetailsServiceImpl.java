package br.com.projeto.docinhos.security;

import br.com.projeto.docinhos.exception.NaoEncontradoException;
import br.com.projeto.docinhos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  @Override
  public UsuarioSecurity loadUserByUsername(String username) {
    return usuarioRepository.findByEmail(username)
      .map(UsuarioSecurity::new)
      .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
  }
}
