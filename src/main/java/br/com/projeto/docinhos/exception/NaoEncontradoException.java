package br.com.projeto.docinhos.exception;

import br.com.projeto.docinhos.enums.CodeError;

public class NaoEncontradoException extends CodeException {
  public NaoEncontradoException(String message) {
    super(CodeError.NAO_ENCONTRADO, message);
  }
}
