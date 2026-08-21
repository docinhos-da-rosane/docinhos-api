package br.com.projeto.docinhos.exception;

import br.com.projeto.docinhos.enums.CodeError;

public class ErroInternoException extends CodeException {
  public ErroInternoException(String message, Throwable cause) {
    super(CodeError.ERRO_INTERNO, message, cause);
  }
}
