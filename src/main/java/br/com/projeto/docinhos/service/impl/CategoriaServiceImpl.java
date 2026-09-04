package br.com.projeto.docinhos.service.impl;

import br.com.projeto.docinhos.dto.CategoriaResponse;
import br.com.projeto.docinhos.mapper.CategoriaMapper;
import br.com.projeto.docinhos.repository.CategoriaRepository;
import br.com.projeto.docinhos.service.CategoriaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

  private final CategoriaRepository categoriaRepository;
  private final CategoriaMapper categoriaMapper;

  @Override
  public List<CategoriaResponse> buscarCategorias() {
    return categoriaRepository.findAll().stream()
        .map(categoriaMapper::toCategoriaResponse)
        .toList();
  }
}
