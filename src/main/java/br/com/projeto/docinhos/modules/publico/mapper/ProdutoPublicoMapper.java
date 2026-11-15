package br.com.projeto.docinhos.modules.publico.mapper;

import br.com.projeto.docinhos.domain.model.Produto;
import br.com.projeto.docinhos.domain.model.ProdutoPorcao;
import br.com.projeto.docinhos.modules.publico.dto.response.ProdutoResumoPublicoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoPublicoMapper {

  @Mapping(target = "id", source = "produto.id")
  @Mapping(target = "nome", source = "produto.nome")
  @Mapping(target = "categoria", source = "produto.categoria.nome")
  @Mapping(target = "disponivel", source = "produto.disponivel")
  @Mapping(target = "porcaoMenorPreco", source = "porcaoMenorPreco")
  @Mapping(target = "imagemMenorUrl", source = "imagemMenorUrl")
  ProdutoResumoPublicoResponse toProdutoResumoPublicoResponse(
      Produto produto, ProdutoPorcao porcaoMenorPreco, String imagemMenorUrl);
}
