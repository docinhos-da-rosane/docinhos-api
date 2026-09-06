package br.com.projeto.docinhos.mapper;

import br.com.projeto.docinhos.dto.request.CriarProdutoPorcaoRequest;
import br.com.projeto.docinhos.model.ProdutoPorcao;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoPorcaoMapper {

  List<ProdutoPorcao> toProdutoPorcaoList(List<CriarProdutoPorcaoRequest> requests);

  ProdutoPorcao toProdutoPorcao(CriarProdutoPorcaoRequest request);
}
