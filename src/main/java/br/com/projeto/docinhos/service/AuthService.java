package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.dto.AuthRequest;
import br.com.projeto.docinhos.dto.AuthResponse;

public interface AuthService {
  AuthResponse logar(AuthRequest request);
}
