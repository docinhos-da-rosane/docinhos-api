package br.com.projeto.docinhos.shared.exception;

import br.com.projeto.docinhos.domain.enums.CodeError;
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
