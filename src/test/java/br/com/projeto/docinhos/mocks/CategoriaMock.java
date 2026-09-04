package br.com.projeto.docinhos.mocks;

import br.com.projeto.docinhos.dto.CategoriaResponse;
import br.com.projeto.docinhos.model.Categoria;
import java.util.UUID;

public class CategoriaMock {

  public static CategoriaResponse criarCategoriaResponsePadrao() {
    return criarCategoriaResponse().build();
  }

  public static CategoriaResponse.CategoriaResponseBuilder criarCategoriaResponse() {
    UUID id = UUID.randomUUID();
    return CategoriaResponse.builder().id(id).nome("Categoria-" + id);
  }

  public static Categoria criarCategoriaPadrao() {
    return criarCategoria().build();
  }

  public static Categoria.CategoriaBuilder criarCategoria() {
    UUID id = UUID.randomUUID();
    return Categoria.builder().id(id).nome("Categoria-" + id);
  }
}
