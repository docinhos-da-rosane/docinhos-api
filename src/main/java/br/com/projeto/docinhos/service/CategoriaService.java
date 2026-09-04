package br.com.projeto.docinhos.service;

import br.com.projeto.docinhos.dto.CategoriaResponse;
import java.util.List;

public interface CategoriaService {
  List<CategoriaResponse> buscarCategorias();
}
