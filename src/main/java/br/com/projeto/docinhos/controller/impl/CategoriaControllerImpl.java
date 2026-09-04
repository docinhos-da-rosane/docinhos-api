package br.com.projeto.docinhos.controller.impl;

import br.com.projeto.docinhos.controller.CategoriaController;
import br.com.projeto.docinhos.dto.CategoriaResponse;
import br.com.projeto.docinhos.service.CategoriaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoriaControllerImpl implements CategoriaController {

  private final CategoriaService categoriaService;

  @Override
  @ResponseStatus(HttpStatus.OK)
  public List<CategoriaResponse> buscarCategorias() {
    return categoriaService.buscarCategorias();
  }
}
