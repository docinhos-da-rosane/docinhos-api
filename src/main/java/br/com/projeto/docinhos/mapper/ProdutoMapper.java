package br.com.projeto.docinhos.mapper;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {CategoriaMapper.class, ProdutoPorcaoMapper.class})
public interface ProdutoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "categoria", ignore = true)
  @Mapping(target = "porcoes", ignore = true)
  Produto toProduto(CriarProdutoRequest request);

  ProdutoResponse toProdutoResponse(Produto produto);
}
