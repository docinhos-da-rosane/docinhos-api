package br.com.projeto.docinhos.modules.admin.service;

import br.com.projeto.docinhos.domain.model.Categoria;
import br.com.projeto.docinhos.modules.admin.dto.response.CategoriaResponse;
import java.util.List;
import java.util.UUID;

public interface CategoriaService {
  List<CategoriaResponse> buscarCategorias();

  Categoria buscarCategoriaPorId(UUID id);
}
