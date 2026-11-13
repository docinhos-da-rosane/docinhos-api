package br.com.projeto.docinhos.security;

import br.com.projeto.docinhos.domain.repository.UsuarioRepository;
import br.com.projeto.docinhos.shared.exception.NaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  @Override
  public SecurityUsuario loadUserByUsername(String username) {
    return usuarioRepository
        .findByEmail(username)
        .map(SecurityUsuario::new)
        .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
  }
}
