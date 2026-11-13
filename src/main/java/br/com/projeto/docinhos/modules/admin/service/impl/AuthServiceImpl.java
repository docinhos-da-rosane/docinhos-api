package br.com.projeto.docinhos.modules.admin.service.impl;

import br.com.projeto.docinhos.modules.admin.dto.response.AuthResponse;
import br.com.projeto.docinhos.modules.admin.service.AuthService;
import br.com.projeto.docinhos.security.JwtService;
import br.com.projeto.docinhos.shared.dto.AuthRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public AuthResponse logar(AuthRequest request) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

    UserDetails usuario = (UserDetails) authentication.getPrincipal();
    String token = jwtService.gerarToken(Objects.requireNonNull(usuario));

    return new AuthResponse(token);
  }
}
