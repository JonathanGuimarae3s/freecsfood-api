CREATE TABLE estado (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE cidade (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    estado_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_cidade_estado (estado_id),
    CONSTRAINT fk_cidade_estado FOREIGN KEY (estado_id) REFERENCES estado (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE cozinha (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE forma_pagamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE permissao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_permissao_nome UNIQUE (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE grupo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_grupo_nome UNIQUE (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_cadastro DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuario_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE restaurante (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    taxa_frete DECIMAL(19, 2) NOT NULL,
    cozinha_id BIGINT NOT NULL,
    data_cadastro DATETIME(6) NOT NULL,
    data_atualizacao DATETIME(6) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    cep VARCHAR(255),
    logradouro VARCHAR(255),
    numero VARCHAR(255),
    complemento VARCHAR(255),
    bairro VARCHAR(255),
    cidade_id BIGINT,
    PRIMARY KEY (id),
    INDEX idx_restaurante_nome (nome),
    INDEX idx_restaurante_cozinha (cozinha_id),
    INDEX idx_restaurante_cidade (cidade_id),
    CONSTRAINT fk_restaurante_cozinha FOREIGN KEY (cozinha_id) REFERENCES cozinha (id),
    CONSTRAINT fk_restaurante_cidade FOREIGN KEY (cidade_id) REFERENCES cidade (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE produto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    preco DECIMAL(19, 2) NOT NULL,
    restaurante_id BIGINT NOT NULL,
    ativo BIT(1) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    INDEX idx_produto_restaurante_ativo (restaurante_id, ativo),
    CONSTRAINT fk_produto_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE pedido (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sub_total DECIMAL(19, 2) NOT NULL,
    taxa_frete DECIMAL(19, 2) NOT NULL,
    valor_total DECIMAL(19, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    data_criacao DATETIME(6) NOT NULL,
    data_confirmacao DATETIME(6),
    data_cancelamento DATETIME(6),
    data_entrega DATETIME(6),
    restaurante_id BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,
    forma_pagamento_id BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    cep VARCHAR(255),
    logradouro VARCHAR(255),
    numero VARCHAR(255),
    complemento VARCHAR(255),
    bairro VARCHAR(255),
    cidade_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_pedido_status_data_criacao (status, data_criacao),
    INDEX idx_pedido_cliente_data_criacao (cliente_id, data_criacao),
    INDEX idx_pedido_restaurante (restaurante_id),
    INDEX idx_pedido_forma_pagamento (forma_pagamento_id),
    INDEX idx_pedido_cidade (cidade_id),
    CONSTRAINT fk_pedido_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante (id),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES usuario (id),
    CONSTRAINT fk_pedido_forma_pagamento FOREIGN KEY (forma_pagamento_id) REFERENCES forma_pagamento (id),
    CONSTRAINT fk_pedido_cidade FOREIGN KEY (cidade_id) REFERENCES cidade (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE item_pedido (
    id BIGINT NOT NULL AUTO_INCREMENT,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(19, 2) NOT NULL,
    preco_total DECIMAL(19, 2) NOT NULL,
    observacao VARCHAR(255),
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_item_pedido_pedido (pedido_id),
    INDEX idx_item_pedido_produto (produto_id),
    CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id),
    CONSTRAINT fk_item_pedido_produto FOREIGN KEY (produto_id) REFERENCES produto (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE grupo_permissao (
    grupo_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    PRIMARY KEY (grupo_id, permissao_id),
    INDEX idx_grupo_permissao_permissao (permissao_id),
    CONSTRAINT fk_grupo_permissao_grupo FOREIGN KEY (grupo_id) REFERENCES grupo (id),
    CONSTRAINT fk_grupo_permissao_permissao FOREIGN KEY (permissao_id) REFERENCES permissao (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuario_grupo (
    usuario_id BIGINT NOT NULL,
    grupo_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, grupo_id),
    INDEX idx_usuario_grupo_grupo (grupo_id),
    CONSTRAINT fk_usuario_grupo_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_usuario_grupo_grupo FOREIGN KEY (grupo_id) REFERENCES grupo (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE restaurante_forma_pagamento (
    restaurante_id BIGINT NOT NULL,
    forma_pagamento_id BIGINT NOT NULL,
    PRIMARY KEY (restaurante_id, forma_pagamento_id),
    INDEX idx_restaurante_forma_pagamento_forma (forma_pagamento_id),
    CONSTRAINT fk_restaurante_forma_pagamento_restaurante
        FOREIGN KEY (restaurante_id) REFERENCES restaurante (id),
    CONSTRAINT fk_restaurante_forma_pagamento_forma
        FOREIGN KEY (forma_pagamento_id) REFERENCES forma_pagamento (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dados de referência da aplicação
INSERT INTO permissao (id, nome, descricao) VALUES
    (1, 'CONSULTAR_RESTAURANTES', 'Permite consultar restaurantes'),
    (2, 'EDITAR_RESTAURANTES', 'Permite editar restaurantes'),
    (3, 'EDITAR_CIDADES', 'Permite editar cidades e estados'),
    (4, 'EDITAR_COZINHAS', 'Permite editar cozinhas');

INSERT INTO grupo (id, nome) VALUES
    (1, 'Administradores');

INSERT INTO grupo_permissao (grupo_id, permissao_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4);

-- Dados de demonstração para estudo e testes manuais
INSERT INTO estado (id, nome) VALUES
    (1, 'São Paulo'),
    (2, 'Minas Gerais'),
    (3, 'Paraná');

INSERT INTO cidade (id, nome, estado_id) VALUES
    (1, 'São Paulo', 1),
    (2, 'Campinas', 1),
    (3, 'Belo Horizonte', 2),
    (4, 'Curitiba', 3);

INSERT INTO cozinha (id, nome) VALUES
    (1, 'Brasileira'),
    (2, 'Italiana'),
    (3, 'Japonesa'),
    (4, 'Mineira');

INSERT INTO forma_pagamento (id, descricao) VALUES
    (1, 'Cartão de crédito'),
    (2, 'Cartão de débito'),
    (3, 'PIX');

INSERT INTO restaurante (
    id, nome, taxa_frete, cozinha_id, data_cadastro, data_atualizacao, version,
    cep, logradouro, numero, complemento, bairro, cidade_id
) VALUES
    (1, 'Sabor da Casa', 8.50, 1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6), 0,
     '01310-100', 'Avenida Paulista', '1000', NULL, 'Bela Vista', 1),
    (2, 'Cantina Central', 6.00, 2, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6), 0,
     '13010-111', 'Rua Barão de Jaguara', '250', NULL, 'Centro', 2),
    (3, 'Sushi Curitiba', 10.90, 3, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6), 0,
     '80010-000', 'Rua XV de Novembro', '500', 'Loja 2', 'Centro', 4),
    (4, 'Tempero Mineiro', 5.50, 4, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6), 0,
     '30130-010', 'Avenida Afonso Pena', '700', NULL, 'Centro', 3);

INSERT INTO produto (id, nome, descricao, preco, restaurante_id, ativo, version) VALUES
    (1, 'Feijoada', 'Feijoada completa com acompanhamentos', 42.90, 1, b'1', 0),
    (2, 'Pudim', 'Pudim de leite condensado', 12.00, 1, b'1', 0),
    (3, 'Lasanha bolonhesa', 'Lasanha artesanal ao molho bolonhesa', 38.50, 2, b'1', 0),
    (4, 'Combinado de sushi', 'Combinado com 20 peças', 59.90, 3, b'1', 0),
    (5, 'Pão de queijo', 'Porção com seis unidades', 16.00, 4, b'1', 0);

INSERT INTO restaurante_forma_pagamento (restaurante_id, forma_pagamento_id) VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 1), (2, 3),
    (3, 1), (3, 2), (3, 3),
    (4, 1), (4, 2), (4, 3);
