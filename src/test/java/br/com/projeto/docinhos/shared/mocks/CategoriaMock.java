package br.com.projeto.docinhos.shared.mocks;

import br.com.projeto.docinhos.domain.model.Categoria;
import br.com.projeto.docinhos.modules.admin.dto.response.CategoriaResponse;
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
