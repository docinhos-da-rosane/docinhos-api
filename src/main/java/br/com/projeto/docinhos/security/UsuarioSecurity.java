package br.com.projeto.docinhos.security;

import br.com.projeto.docinhos.model.Usuario;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioSecurity implements UserDetails {

  private final Usuario usuario;

  public UsuarioSecurity(Usuario usuario) {
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
