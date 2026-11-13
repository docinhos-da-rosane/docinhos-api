package br.com.projeto.docinhos.modules.admin.service.impl;

import br.com.projeto.docinhos.domain.model.Categoria;
import br.com.projeto.docinhos.domain.repository.CategoriaRepository;
import br.com.projeto.docinhos.modules.admin.dto.response.CategoriaResponse;
import br.com.projeto.docinhos.modules.admin.mapper.CategoriaMapper;
import br.com.projeto.docinhos.modules.admin.service.CategoriaService;
import br.com.projeto.docinhos.shared.exception.NaoEncontradoException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

  private final CategoriaRepository categoriaRepository;
  private final CategoriaMapper categoriaMapper;

  @Override
  public List<CategoriaResponse> buscarCategorias() {
    return categoriaRepository.findAllByOrderByNomeAsc().stream()
        .map(categoriaMapper::toCategoriaResponse)
        .toList();
  }

  @Override
  public Categoria buscarCategoriaPorId(UUID id) {
    return categoriaRepository
        .findById(id)
        .orElseThrow(() -> new NaoEncontradoException("Categoria não encontrada: id=" + id));
  }
}
