package br.com.projeto.docinhos.mapper;

import br.com.projeto.docinhos.dto.request.ProdutoPorcaoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoPorcaoResponse;
import br.com.projeto.docinhos.model.ProdutoPorcao;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoPorcaoMapper {

  List<ProdutoPorcao> toProdutoPorcaoList(List<ProdutoPorcaoRequest> requests);

  @Mapping(target = "id", ignore = true)
  ProdutoPorcao toProdutoPorcao(ProdutoPorcaoRequest request);

  List<ProdutoPorcaoResponse> toProdutoPorcaoResponseList(List<ProdutoPorcao> porcoes);

  ProdutoPorcaoResponse toProdutoPorcaoResponse(ProdutoPorcao produtoPorcao);
}
