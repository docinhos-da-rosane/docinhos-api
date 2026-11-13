package br.com.projeto.docinhos.modules.admin.service;

import br.com.projeto.docinhos.modules.admin.dto.response.AuthResponse;
import br.com.projeto.docinhos.shared.dto.AuthRequest;

public interface AuthService {
  AuthResponse logar(AuthRequest request);
}
