COMMENT ON TABLE tb_produtos_imagens IS
    'Armazena a imagem associada a cada produto.';

COMMENT ON COLUMN tb_produtos_imagens.id IS
    'Identificador único da imagem do produto.';

COMMENT ON COLUMN tb_produtos_imagens.url IS
    'URL pública da imagem armazenada no serviço externo.';

COMMENT ON COLUMN tb_produtos_imagens.id_armazenamento IS
    'Identificador da imagem no serviço externo de armazenamento.';

COMMENT ON COLUMN tb_produtos_imagens.id_produto IS
    'Identificador do produto associado à imagem.';
