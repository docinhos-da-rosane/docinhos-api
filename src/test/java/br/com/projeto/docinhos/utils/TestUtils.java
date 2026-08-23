package br.com.projeto.docinhos.utils;

import br.com.projeto.docinhos.exception.ErroInternoException;
import tools.jackson.databind.ObjectMapper;

public class TestUtils {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private TestUtils() {}

  public static String toJson(Object object) {
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (Exception e) {
      throw new ErroInternoException("Erro ao converter objeto para JSON: " + e.getMessage(), e);
    }
  }
}
