PRAGMA foreign_keys = ON;

-- Categorias de resíduo eletrônico --
CREATE TABLE categoria_residuos_proj_Sandra (
  id_categoria         INTEGER PRIMARY KEY AUTOINCREMENT,
  nome                 VARCHAR(50)  NOT NULL,
  descricao            TEXT,
  instrucoes_seguranca VARCHAR(100) NOT NULL
);

-- Pontos de coleta --
CREATE TABLE pontos_coleta_projeto_Sandra (
  id_ponto     INTEGER PRIMARY KEY AUTOINCREMENT,
  nome         VARCHAR(100) NOT NULL,
  endereco     VARCHAR(300) UNIQUE NOT NULL,
  lat          REAL         NOT NULL,
  lgn          REAL         NOT NULL,           -- coluna é "lgn", não "lng"
  id_categoria INTEGER      NOT NULL,
  UNIQUE (lat, lgn),
  FOREIGN KEY (id_categoria) REFERENCES categoria_residuos_proj_Sandra(id_categoria)
);

-- Interações/consultas --
-- CORREÇÃO: FK apontava para "pontos_coleta_proj" (inexistente)
--           Corrigido para "pontos_coleta_projeto_Sandra"
CREATE TABLE usuarios_interacao_proj_Sandra (
  id_consulta     INTEGER PRIMARY KEY AUTOINCREMENT,
  id_ponto        INTEGER NOT NULL,
  data_consulta   TEXT    NOT NULL DEFAULT (datetime('now')),
  regiao_estimada VARCHAR(150),
  FOREIGN KEY (id_ponto) REFERENCES pontos_coleta_projeto_Sandra(id_ponto)
);
