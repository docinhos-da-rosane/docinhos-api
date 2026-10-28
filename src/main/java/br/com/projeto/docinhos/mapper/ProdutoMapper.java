package br.com.projeto.docinhos.mapper;

import br.com.projeto.docinhos.dto.request.ProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoDetalhadoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResumoResponse;
import br.com.projeto.docinhos.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = {CategoriaMapper.class, ProdutoPorcaoMapper.class})
public interface ProdutoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "categoria", ignore = true)
  @Mapping(target = "porcoes", ignore = true)
  @Mapping(target = "imagem", ignore = true)
  @Mapping(target = "disponivel", ignore = true)
  Produto toProduto(ProdutoRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "categoria", ignore = true)
  @Mapping(target = "porcoes", ignore = true)
  @Mapping(target = "imagem", ignore = true)
  @Mapping(target = "disponivel", ignore = true)
  void toProduto(ProdutoRequest request, @MappingTarget Produto produto);

  @Mapping(target = "imagemUrl", source = "imagem.url")
  ProdutoResponse toProdutoResponse(Produto produto);

  @Mapping(target = "categoria", source = "produto.categoria.nome")
  @Mapping(target = "imagemMiniaturaUrl", source = "imagemMiniaturaUrl")
  ProdutoResumoResponse toProdutoResumoResponse(Produto produto, String imagemMiniaturaUrl);

  @Mapping(target = "categoria", source = "produto.categoria")
  @Mapping(target = "imagemUrl", source = "imagemMediaUrl")
  ProdutoDetalhadoResponse toProdutoDetalhadoResponse(Produto produto, String imagemMediaUrl);
}
