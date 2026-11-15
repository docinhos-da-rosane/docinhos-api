package br.com.projeto.docinhos.modules.publico.service.impl;

import br.com.projeto.docinhos.domain.model.Produto;
import br.com.projeto.docinhos.domain.model.ProdutoPorcao;
import br.com.projeto.docinhos.domain.repository.ProdutoRepository;
import br.com.projeto.docinhos.modules.publico.dto.response.ProdutoResumoPublicoResponse;
import br.com.projeto.docinhos.modules.publico.mapper.ProdutoPublicoMapper;
import br.com.projeto.docinhos.modules.publico.service.ProdutoImagemPublicoService;
import br.com.projeto.docinhos.modules.publico.service.ProdutoPublicoService;
import br.com.projeto.docinhos.shared.dto.PageResponse;
import br.com.projeto.docinhos.shared.mapper.PageMapper;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("produtoPublicoServiceImpl")
@RequiredArgsConstructor
public class ProdutoPublicoServiceImpl implements ProdutoPublicoService {

  private final ProdutoRepository produtoRepository;
  private final ProdutoPublicoMapper produtoPublicoMapper;
  private final ProdutoImagemPublicoService produtoImagemPublicoService;
  private final PageMapper pageMapper;

  @Override
  @Transactional(readOnly = true)
  public PageResponse<ProdutoResumoPublicoResponse> listarProdutos(int page, int size) {
    Sort ordenacao = Sort.by(Sort.Order.desc("disponivel"), Sort.Order.asc("nome"));
    PageRequest paginacao = PageRequest.of(page, size, ordenacao);

    Page<ProdutoResumoPublicoResponse> produtos =
        produtoRepository.findAll(paginacao).map(this::toProdutoResumoPublicoResponse);

    return pageMapper.toPageResponse(produtos);
  }

  private ProdutoResumoPublicoResponse toProdutoResumoPublicoResponse(Produto produto) {
    ProdutoPorcao porcaoMenorPreco =
        produto.getPorcoes().stream()
            .min(Comparator.comparing(ProdutoPorcao::getPreco))
            .orElseThrow();

    String imagemMenorUrl = produtoImagemPublicoService.gerarUrlMenor(produto.getImagem());

    return produtoPublicoMapper.toProdutoResumoPublicoResponse(
        produto, porcaoMenorPreco, imagemMenorUrl);
  }
}
