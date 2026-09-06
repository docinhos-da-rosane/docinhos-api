CREATE TABLE tb_produtos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    id_categoria UUID NOT NULL,
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produtos_categoria
     FOREIGN KEY (id_categoria)
       REFERENCES tb_categorias (id)
);
