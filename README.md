# ♻️ Resíduos Eletrônicos — Backend

API REST desenvolvida em **Java + Spring Boot** para o projeto integrador do curso de ADS.  
Conecta usuários aos pontos de descarte de lixo eletrônico mais próximos usando geolocalização.

---

## 🚀 Como rodar

### Requisitos
- Java 17+
- Maven 3.8+

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/SEU_USUARIO/residuos-eletronicos-backend.git

# 2. Entre na pasta
cd residuos-eletronicos-backend

# 3. Rode o servidor
mvn spring-boot:run
```

A API sobe em: **http://localhost:8080**

> O banco de dados (`residuos_eletronicos.db`) é criado automaticamente na primeira execução.  
> Dados iniciais (categorias e pontos de exemplo) são inseridos automaticamente pelo DataLoader.

---

## 📡 Endpoints da API

### Pontos de Coleta

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/pontos` | Lista todos os pontos cadastrados |
| `GET` | `/api/pontos/proximos?lat=&lgn=&raio=` | Busca pontos próximos ordenados por distância |
| `POST` | `/api/pontos` | Cadastra um novo ponto |
| `DELETE` | `/api/pontos/{id}` | Remove um ponto pelo ID |

### Categorias

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/categorias` | Lista todas as categorias |
| `POST` | `/api/categorias` | Cadastra uma nova categoria |
| `DELETE` | `/api/categorias/{id}` | Remove uma categoria pelo ID |

---

## 🌐 Exemplo de uso no Frontend

```javascript
// Captura a localização do usuário e busca os pontos mais próximos
navigator.geolocation.getCurrentPosition(async (pos) => {
  const { latitude, longitude } = pos.coords;

  const resposta = await fetch(
    `http://localhost:8080/api/pontos/proximos?lat=${latitude}&lgn=${longitude}&raio=10`
  );

  const pontos = await resposta.json();
  console.log(pontos); // lista ordenada do mais próximo ao mais distante
});
```

> ⚠️ **Atenção:** o parâmetro de longitude é `lgn` (não `lng`) — igual ao nome da coluna no banco.

---

## 📦 Exemplo de Body para POST

### Cadastrar categoria
```json
{
  "nome": "Baterias e Pilhas",
  "descricao": "Pilhas alcalinas, baterias de lítio e de celular.",
  "instrucoesSeguranaca": "Não perfurar ou incinerar. Embalar individualmente."
}
```

### Cadastrar ponto de coleta
```json
{
  "nome": "Poupatempo Sé",
  "endereco": "Praça do Carmo, s/n - Sé, São Paulo - SP",
  "lat": -23.5489,
  "lgn": -46.6338,
  "categoria": { "idCategoria": 1 }
}
```

---

## 🗄️ Banco de Dados

O arquivo `Main_corrigido.sql` contém a estrutura completa das tabelas.  
As tabelas são criadas automaticamente pelo Hibernate ao iniciar — você **não precisa rodar o SQL manualmente**.

| Tabela | Função |
|--------|--------|
| `categoria_residuos_proj_Sandra` | Tipos de resíduos aceitos |
| `pontos_coleta_projeto_Sandra` | Locais físicos de descarte |
| `usuarios_interacao_proj_Sandra` | Registro de consultas (métricas) |

---

## 🏗️ Estrutura do Projeto

```
src/main/java/br/edu/ads/residuos/
├── config/
│   └── DataLoader.java          # Popula o banco com dados iniciais
├── controller/
│   ├── CategoriaController.java # Endpoints de categorias
│   └── PontoColetaController.java # Endpoints de pontos
├── model/
│   ├── Categoria.java
│   ├── PontoColeta.java
│   └── UsuarioInteracao.java
├── repository/
│   ├── CategoriaRepository.java
│   ├── PontoColetaRepository.java
│   └── UsuarioInteracaoRepository.java
├── service/
│   └── PontoColetaService.java  # Lógica de busca por proximidade (Haversine)
├── util/
│   └── HaversineUtil.java       # Fórmula de distância geográfica
└── ResiduosEletronicosApplication.java
```

---

## 👩‍💻 Autora

Desenvolvido por **Sandra** — Curso de ADS  
Projeto Integrador: Mapeamento de Pontos de Descarte de Resíduos Eletrônicos
