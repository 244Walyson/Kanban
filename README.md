# Kanban Application API

Este repositório contém a definição OpenAPI para a API da Aplicação Kanban. A API é projetada para gerenciar usuários, equipes, quadros e tarefas, em um sistema de gerenciamento de projetos no estilo Kanban.

## Visão Geral

A API da Aplicação Kanban fornece endpoints para gerenciar diversos recursos dentro da aplicação, incluindo usuários, equipes, quadros e tarefas. Esta documentação descreve os principais endpoints, seus parâmetros de requisição e respostas esperadas.

## Configuração
### Rodando localmente como Docker

##### Faça o clone do projeto e rode o script do docker compose
*Certifique de ter o docker e o docker compose instalado*

```shell
git clone -b local git@github.com:244Walyson/Kanban-services.git
cd Kanban-services
docker-compose up --build -d
```
Após o container kafka Connect subir instale o conector do mysql
```shell
cd Kubernets-Docker-Configs/kafka-connect
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @mysql.json
cd .. && cd ..
```

## URL Base

A URL base para todas as requisições da API é: 
se estiver rodando localmente:
```bash
http://localhost:8080
```

## Endpoints Disponíveis

### Endpoints de Usuário

- Atualizar dados do usuário
- Atualizar imagem do usuário
- Obter todos os usuários
- Criar novo usuário
- Conectar usuário
- Aprovar usuário
- Obter dados do usuário logado
- Obter token de acesso

### Endpoints de Equipe

- Obter detalhes de uma equipe
- Atualizar dados da equipe
- Deletar equipe
- Obter todas as equipes
- Criar nova equipe
- Adicionar usuário à equipe

### Endpoints de Quadro

- Obter detalhes de um quadro
- Atualizar dados do quadro
- Deletar quadro
- Obter todos os quadros de uma equipe
- Criar novo quadro

### Endpoints de tarefa

- Obter detalhes de uma tarefa
- Atualizar dados da tarefa
- Deletar tarefa
- Criar nova tarefa
- Definir colaborador para uma tarefa
- Reordenar tarefa em um quadro

### Endpoints de Arquivo

- Upload de imagem

## Modelos de Dados

A API utiliza diversos modelos de dados para as requisições e respostas, incluindo:

- `UserDTO`, `UserUpdateDTO`, `UserInsertDTO`, `UserMinDTO`, `UserLoggedDTO`
- `TeamDTO`, `TeamInsertDTO`
- `BoardDTO`, `BoardUpdateDTO`, `BoardInsertDTO`, `BoardMinDTO`, `BoardProjection`
- `CardDTO`, `CardUpdateDTO`, `CardInsertDTO`
- `UriDTO`, `AddUserDTO`, `SetCollaboratorDTO`, `ReplacementDTO`
- `AccessToken`, `Pageable`, `SortObject`, `PageTeamDTO`, `PageBoardProjection`

## Documentação Completa

Para mais detalhes sobre todos os endpoints disponíveis, parâmetros, esquemas de requisição e resposta, consulte a [documentação Swagger](http://kanban.rancher.waly.dev.br/swagger-ui.html) da aplicação.


