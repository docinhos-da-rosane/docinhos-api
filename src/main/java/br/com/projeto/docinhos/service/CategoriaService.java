package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.dto.CategoriaResponse;
import br.com.projeto.docinhos.model.Categoria;
import java.util.List;
import java.util.UUID;

public interface CategoriaService {
  List<CategoriaResponse> buscarCategorias();

  Categoria buscarCategoriaPorId(UUID id);
}
