package br.com.projeto.docinhos.service.impl;

import br.com.projeto.docinhos.dto.request.CriarProdutoRequest;
import br.com.projeto.docinhos.dto.response.ProdutoResponse;
import br.com.projeto.docinhos.mapper.ProdutoMapper;
import br.com.projeto.docinhos.mapper.ProdutoPorcaoMapper;
import br.com.projeto.docinhos.model.Categoria;
import br.com.projeto.docinhos.model.Produto;
import br.com.projeto.docinhos.model.ProdutoPorcao;
import br.com.projeto.docinhos.repository.ProdutoRepository;
import br.com.projeto.docinhos.service.CategoriaService;
import br.com.projeto.docinhos.service.ProdutoService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

  private final CategoriaService categoriaService;
  private final ProdutoRepository produtoRepository;
  private final ProdutoMapper produtoMapper;
  private final ProdutoPorcaoMapper produtoPorcaoMapper;

  @Override
  @Transactional
  public ProdutoResponse criarProduto(CriarProdutoRequest request) {
    log.info("Iniciando cadastro de produto: nome={}", request.nome());

    Categoria categoria = categoriaService.buscarCategoriaPorId(request.categoriaId());

    Produto produto = produtoMapper.toProduto(request);
    List<ProdutoPorcao> porcoes = produtoPorcaoMapper.toProdutoPorcaoList(request.porcoes());

    produto.setCategoria(categoria);
    produto.adicionarPorcoes(porcoes);

    Produto produtoSalvo = produtoRepository.save(produto);

    log.info(
        "Produto cadastrado com sucesso: produtoId={}, nome={}",
        produtoSalvo.getId(),
        produtoSalvo.getNome());

    return produtoMapper.toProdutoResponse(produtoSalvo);
  }
}
