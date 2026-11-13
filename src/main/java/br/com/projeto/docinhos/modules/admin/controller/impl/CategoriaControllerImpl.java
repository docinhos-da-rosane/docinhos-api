package br.com.projeto.docinhos.modules.admin.controller.impl;

import br.com.projeto.docinhos.modules.admin.controller.CategoriaController;
import br.com.projeto.docinhos.modules.admin.dto.response.CategoriaResponse;
import br.com.projeto.docinhos.modules.admin.service.CategoriaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoriaControllerImpl implements CategoriaController {

  private final CategoriaService categoriaService;

  @Override
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<CategoriaResponse> buscarCategorias() {
    return categoriaService.buscarCategorias();
  }
}
