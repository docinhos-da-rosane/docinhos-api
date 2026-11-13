package br.com.projeto.docinhos.modules.admin.mapper;

import br.com.projeto.docinhos.domain.model.Categoria;
import br.com.projeto.docinhos.modules.admin.dto.response.CategoriaResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
  CategoriaResponse toCategoriaResponse(Categoria categoria);
}
