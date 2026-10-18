package br.com.projeto.docinhos.service.impl;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.PageResponse;
import br.com.projeto.docinhos.dto.response.ProdutoDetalhadoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.dto.response.ProdutoResumoResponse;
import br.com.projeto.docinhos.exception.NaoEncontradoException;
import br.com.projeto.docinhos.mapper.PageMapper;
import br.com.projeto.docinhos.mapper.ProdutoMapper;
import br.com.projeto.docinhos.mapper.ProdutoPorcaoMapper;
import br.com.projeto.docinhos.model.Categoria;
import br.com.projeto.docinhos.model.Produto;
import br.com.projeto.docinhos.model.ProdutoImagem;
import br.com.projeto.docinhos.model.ProdutoPorcao;
import br.com.projeto.docinhos.repository.ProdutoRepository;
import br.com.projeto.docinhos.service.CategoriaService;
import br.com.projeto.docinhos.service.ProdutoImagemService;
import br.com.projeto.docinhos.service.ProdutoService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

  private final CategoriaService categoriaService;
  private final ProdutoImagemService produtoImagemService;
  private final ProdutoRepository produtoRepository;
  private final ProdutoMapper produtoMapper;
  private final ProdutoPorcaoMapper produtoPorcaoMapper;
  private final PageMapper pageMapper;

  @Override
  @Transactional
  public ProdutoResponse criarProduto(MultipartFile imagem, CriarProdutoRequest request) {
    log.info("Iniciando cadastro de produto: nome={}", request.nome());

    Categoria categoria = categoriaService.buscarCategoriaPorId(request.categoriaId());

    ProdutoImagem produtoImagem = null;
    try {
      produtoImagem = produtoImagemService.armazenar(imagem);

      Produto produto = produtoMapper.toProduto(request);
      List<ProdutoPorcao> porcoes = produtoPorcaoMapper.toProdutoPorcaoList(request.porcoes());

      produto.setCategoria(categoria);
      produto.adicionarPorcoes(porcoes);
      produto.adicionarImagem(produtoImagem);

      Produto produtoSalvo = produtoRepository.save(produto);

      log.info(
          "Produto cadastrado com sucesso: produtoId={}, nome={}",
          produtoSalvo.getId(),
          produtoSalvo.getNome());

      return produtoMapper.toProdutoResponse(produtoSalvo);

    } catch (Exception e) {
      produtoImagemService.rollback(produtoImagem);
      throw e;
    }
  }

  @Override
  public PageResponse<ProdutoResumoResponse> listarProdutos(int page, int size) {
    Sort ordenacao = Sort.by(Sort.Order.desc("disponivel"), Sort.Order.asc("nome"));
    PageRequest paginacao = PageRequest.of(page, size, ordenacao);

    Page<ProdutoResumoResponse> produtosResumo =
        produtoRepository.findAll(paginacao).map(this::toProdutoResumoResponse);

    return pageMapper.toPageResponse(produtosResumo);
  }

  @Override
  public ProdutoDetalhadoResponse buscarProduto(UUID id) {
    Produto produto =
        produtoRepository
            .findProdutoById(id)
            .orElseThrow(() -> new NaoEncontradoException("Produto não encontrado: id=" + id));

    return toProdutoDetalhadoResponse(produto);
  }

  private ProdutoResumoResponse toProdutoResumoResponse(Produto produto) {
    String imagemMiniaturaUrl = produtoImagemService.gerarUrlMiniatura(produto.getImagem());
    return produtoMapper.toProdutoResumoResponse(produto, imagemMiniaturaUrl);
  }

  private ProdutoDetalhadoResponse toProdutoDetalhadoResponse(Produto produto) {
    String imagemMediaUrl = produtoImagemService.gerarUrlMedia(produto.getImagem());
    return produtoMapper.toProdutoDetalhadoResponse(produto, imagemMediaUrl);
  }
}
