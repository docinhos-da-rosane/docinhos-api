package br.com.projeto.docinhos.shared.mocks;

import br.com.projeto.docinhos.modules.admin.dto.response.AuthResponse;
import br.com.projeto.docinhos.shared.dto.AuthRequest;

public class AuthMock {
  public static final String EMAIL_VALIDO = "maria_docinhos@email.com";
  public static final String SENHA_VALIDA = "123456";
  public static final String TOKEN_VALIDO = "token-jwt";

  public static AuthRequest criarAuthRequestPadrao() {
    return criarAuthRequest().build();
  }

  public static AuthRequest.AuthRequestBuilder criarAuthRequest() {
    return AuthRequest.builder().email(EMAIL_VALIDO).senha(SENHA_VALIDA);
  }

  public static AuthResponse criarAuthResponsePadrao() {
    return new AuthResponse(TOKEN_VALIDO);
  }
}
