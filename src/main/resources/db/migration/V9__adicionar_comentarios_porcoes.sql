COMMENT ON TABLE tb_produtos_porcoes IS
  'Tabela responsável por armazenar as porções e seus respectivos preços vinculados aos produtos do catálogo.';

COMMENT ON COLUMN tb_produtos_porcoes.id IS
  'Identificador único da porção do produto.';

COMMENT ON COLUMN tb_produtos_porcoes.quantidade IS
  'Quantidade de unidades correspondente à porção do produto.';

COMMENT ON COLUMN tb_produtos_porcoes.preco IS
  'Preço da porção do produto.';

COMMENT ON COLUMN tb_produtos_porcoes.id_produto IS
  'Identificador do produto ao qual a porção pertence.';
