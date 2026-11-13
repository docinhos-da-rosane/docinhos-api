package br.com.projeto.docinhos.modules.admin.controller.impl;

import br.com.projeto.docinhos.modules.admin.controller.AuthController;
import br.com.projeto.docinhos.modules.admin.dto.response.AuthResponse;
import br.com.projeto.docinhos.modules.admin.service.AuthService;
import br.com.projeto.docinhos.shared.dto.AuthRequest;
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
