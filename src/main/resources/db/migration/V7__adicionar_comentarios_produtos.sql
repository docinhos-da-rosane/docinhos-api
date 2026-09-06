COMMENT ON TABLE tb_produtos IS
  'Tabela responsável por armazenar os produtos disponíveis no catálogo.';

COMMENT ON COLUMN tb_produtos.id IS
  'Identificador único do produto.';

COMMENT ON COLUMN tb_produtos.nome IS
  'Nome do produto exibido no catálogo.';

COMMENT ON COLUMN tb_produtos.descricao IS
  'Descrição do produto exibida no catálogo.';

COMMENT ON COLUMN tb_produtos.disponivel IS
  'Indica se o produto está disponível para encomenda.';

COMMENT ON COLUMN tb_produtos.id_categoria IS
  'Identificador da categoria utilizada para classificar o produto.';

COMMENT ON COLUMN tb_produtos.data_criacao IS
  'Data e hora em que o produto foi cadastrado.';

COMMENT ON COLUMN tb_produtos.data_atualizacao IS
  'Data e hora da última atualização do produto.';
