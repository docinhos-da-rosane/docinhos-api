package br.com.projeto.docinhos.exception;

import br.com.projeto.docinhos.enums.CodeError;

public class RequisicaoInvalidaException extends CodeException {
  public RequisicaoInvalidaException(String message) {
    super(CodeError.DADOS_INVALIDOS, message);
  }
}
