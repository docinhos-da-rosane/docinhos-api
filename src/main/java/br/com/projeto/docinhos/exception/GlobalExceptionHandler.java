package br.com.projeto.docinhos.exception;

import br.com.projeto.docinhos.dto.ErrorResponse;
import br.com.projeto.docinhos.enums.CodeError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception, HttpServletRequest request) {

    FieldError fieldError = exception.getBindingResult().getFieldErrors().getFirst();

    log.error("Ocorreu um erro de validação: {}", fieldError.getDefaultMessage(), exception);

    ErrorResponse error =
        new ErrorResponse(
            CodeError.DADOS_INVALIDOS, fieldError.getDefaultMessage(), request.getRequestURI());

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(ErroInternoException.class)
  public ResponseEntity<ErrorResponse> handleErroInternoException(
      ErroInternoException exception, HttpServletRequest request) {
    log.error("Ocorreu um erro interno: {}", exception.getMessage(), exception);

    ErrorResponse error =
        new ErrorResponse(
            exception.getCodeError(), exception.getMessage(), request.getRequestURI());

    return ResponseEntity.internalServerError().body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception exception, HttpServletRequest request) {
    log.error("Ocorreu um erro genérico: {}", exception.getMessage(), exception);

    ErrorResponse error =
        new ErrorResponse(CodeError.ERRO_INTERNO, exception.getMessage(), request.getRequestURI());

    return ResponseEntity.internalServerError().body(error);
  }
}
