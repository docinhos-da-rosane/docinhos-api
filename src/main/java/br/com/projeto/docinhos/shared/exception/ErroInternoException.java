package br.com.projeto.docinhos.shared.exception;

import br.com.projeto.docinhos.domain.enums.CodeError;

public class ErroInternoException extends CodeException {
  public ErroInternoException(String message, Throwable cause) {
    super(CodeError.ERRO_INTERNO, message, cause);
  }
}
