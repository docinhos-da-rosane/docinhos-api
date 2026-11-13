package br.com.projeto.docinhos.shared.exception;

import br.com.projeto.docinhos.domain.enums.CodeError;

public class NaoEncontradoException extends CodeException {
  public NaoEncontradoException(String message) {
    super(CodeError.NAO_ENCONTRADO, message);
  }
}
