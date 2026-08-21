package br.com.projeto.docinhos.security;

import br.com.projeto.docinhos.model.Usuario;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUsuario implements UserDetails {

  private final Usuario usuario;

  public SecurityUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public @Nullable String getPassword() {
    return usuario.getSenha();
  }

  @Override
  public String getUsername() {
    return usuario.getEmail();
  }
}
