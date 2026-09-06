CREATE TABLE tb_produtos_porcoes (
    id UUID PRIMARY KEY,
    quantidade INTEGER NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    id_produto UUID NOT NULL,

    CONSTRAINT fk_produtos_porcoes_produto
     FOREIGN KEY (id_produto)
       REFERENCES tb_produtos (id)
       ON DELETE CASCADE
);
