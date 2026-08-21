package br.com.projeto.docinhos.service.impl;

import br.com.projeto.docinhos.dto.AuthRequest;
import br.com.projeto.docinhos.dto.AuthResponse;
import br.com.projeto.docinhos.security.JwtService;
import br.com.projeto.docinhos.service.AuthService;
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
