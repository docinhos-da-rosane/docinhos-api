package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.dto.request.AuthRequest;
import br.com.projeto.docinhos.dto.response.AuthResponse;

public interface AuthService {
  AuthResponse logar(AuthRequest request);
}
