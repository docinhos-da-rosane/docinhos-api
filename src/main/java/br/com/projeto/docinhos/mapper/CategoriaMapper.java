package br.com.projeto.docinhos.mapper;

import br.com.projeto.docinhos.dto.CategoriaResponse;
import br.com.projeto.docinhos.model.Categoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
  CategoriaResponse toCategoriaResponse(Categoria categoria);
}
