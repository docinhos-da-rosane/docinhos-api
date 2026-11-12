package br.com.projeto.docinhos.service.impl;

import br.com.projeto.docinhos.dto.request.ProdutoDisponibilidadeRequest;
import br.com.projeto.docinhos.dto.request.ProdutoRequest;
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
import java.util.Objects;
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
  public ProdutoResponse criarProduto(MultipartFile imagem, ProdutoRequest request) {
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
    Produto produto = buscarProdutoPorId(id);

    return toProdutoDetalhadoResponse(produto);
  }

  @Override
  @Transactional
  public void atualizarProduto(UUID id, MultipartFile imagem, ProdutoRequest request) {
    log.info("Iniciando atualização de produto: id={}", id);

    Produto produto = buscarProdutoPorId(id);
    Categoria categoria = categoriaService.buscarCategoriaPorId(request.categoriaId());

    boolean tentativaExclusaoImagem = false;
    ProdutoImagem produtoImagemNova = null;
    ProdutoImagem produtoImagemAnterior = produto.getImagem();
    String idArmazenamentoAnterior = produtoImagemAnterior.getIdArmazenamento();

    try {

      atualizarProdutoComDadosBasicos(request, produto, categoria);

      if (Objects.nonNull(imagem)) {
        produtoImagemNova = atualizarProdutoImagem(imagem, produto);

        tentativaExclusaoImagem = true;
        produtoImagemService.deletar(produtoImagemAnterior);
      }

      produtoRepository.flush();

    } catch (Exception ex) {

      if (Objects.nonNull(produtoImagemNova)) {
        produtoImagemService.rollback(produtoImagemNova);
      }

      if (tentativaExclusaoImagem) {
        restaurar(idArmazenamentoAnterior);
      }

      throw ex;
    }

    log.info(
        "Produto atualizado com sucesso: produtoId={}, nome={}",
        produto.getId(),
        produto.getNome());
  }

  @Override
  @Transactional
  public void alterarDisponibilidade(UUID id, ProdutoDisponibilidadeRequest request) {
    log.info(
        "Alterando disponibilidade do produto: id={}, disponivel={}", id, request.disponivel());

    Produto produto = buscarProdutoPorId(id);
    produto.setDisponivel(request.disponivel());

    produtoRepository.flush();
  }

  @Override
  @Transactional
  public void deletarProduto(UUID id) {
    Produto produto = buscarProdutoPorId(id);

    ProdutoImagem imagem = produto.getImagem();
    String idArmazenamento = imagem.getIdArmazenamento();
    boolean tentativaExclusaoImagem = false;

    try {
      produtoRepository.delete(produto);

      tentativaExclusaoImagem = true;

      produtoImagemService.deletar(imagem);
      produtoRepository.flush();

    } catch (Exception ex) {
      if (tentativaExclusaoImagem) {
        restaurar(idArmazenamento);
      }

      throw ex;
    }
  }

  private void atualizarProdutoComDadosBasicos(
      ProdutoRequest request, Produto produto, Categoria categoria) {
    produtoMapper.toProduto(request, produto);
    produto.setCategoria(categoria);

    List<ProdutoPorcao> porcoes = produtoPorcaoMapper.toProdutoPorcaoList(request.porcoes());
    produto.removerPorcoes();
    produto.adicionarPorcoes(porcoes);
  }

  private ProdutoImagem atualizarProdutoImagem(MultipartFile imagemNova, Produto produto) {
    ProdutoImagem produtoImagemNova = produtoImagemService.armazenar(imagemNova);
    produto.removerImagem();
    produto.adicionarImagem(produtoImagemNova);

    return produtoImagemNova;
  }

  private void restaurar(String idArmazenamento) {
    try {
      produtoImagemService.restaurar(idArmazenamento);
    } catch (Exception falhaRestauracao) {
      log.error(
          "Falha ao restaurar imagem do produto após erro na exclusão. idArmazenamento={}",
          idArmazenamento,
          falhaRestauracao);
    }
  }

  private Produto buscarProdutoPorId(UUID id) {
    return produtoRepository
        .findProdutoById(id)
        .orElseThrow(() -> new NaoEncontradoException("Produto não encontrado: id=" + id));
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
