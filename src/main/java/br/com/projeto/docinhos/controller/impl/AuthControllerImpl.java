package br.com.projeto.docinhos.controller.impl;

import br.com.projeto.docinhos.controller.AuthController;
import br.com.projeto.docinhos.dto.AuthRequest;
import br.com.projeto.docinhos.dto.AuthResponse;
import br.com.projeto.docinhos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthControllerImpl implements AuthController {

  private final AuthService authService;

  @Override
  @ResponseStatus(HttpStatus.OK)
  public AuthResponse logar(AuthRequest request) {
    return authService.logar(request);
  }
}
