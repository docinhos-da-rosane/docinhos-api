CREATE TABLE tb_produtos_imagens (
     id UUID PRIMARY KEY,
     url VARCHAR(500) NOT NULL,
     id_armazenamento VARCHAR(255) NOT NULL,
     id_produto UUID NOT NULL UNIQUE,

     CONSTRAINT fk_produtos_imagens_produto
       FOREIGN KEY (id_produto)
         REFERENCES tb_produtos(id)
         ON DELETE CASCADE
);
