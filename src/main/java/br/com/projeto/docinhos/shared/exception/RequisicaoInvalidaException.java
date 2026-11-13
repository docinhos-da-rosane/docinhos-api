package br.com.projeto.docinhos.shared.exception;

import br.com.projeto.docinhos.domain.enums.CodeError;

public class RequisicaoInvalidaException extends CodeException {
  public RequisicaoInvalidaException(String message) {
    super(CodeError.DADOS_INVALIDOS, message);
  }
}
