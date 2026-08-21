package br.com.projeto.docinhos.exception;

import br.com.projeto.docinhos.enums.CodeError;
import lombok.Getter;

@Getter
public class CodeException extends RuntimeException {

  private CodeError codeError;

  public CodeException(CodeError codeError, String message) {
    super(message);
    this.codeError = codeError;
  }

  public CodeException(CodeError codeError, String message, Throwable cause) {
    super(message, cause);
    this.codeError = codeError;
  }
}
