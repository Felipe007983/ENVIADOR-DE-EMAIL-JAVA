# Plataforma de Envio de Emails com Java, Spring Boot e Thymeleaf

Uma plataforma de envio de emails a partir de arquivos CSV utilizando Java, Spring Boot, e Thymeleaf. A aplicação se comunica com um banco de dados SQL e oferece um front-end em HTML, CSS e Bootstrap para fácil navegação e configuração.

## Funcionalidades

### 1. Login e Gerenciamento de Acesso
- Permite que os usuários façam login, registrem-se ou redefinam a senha.

### 2. Configuração SMTP
- Opção para configurar as definições SMTP para conexão de email.
- Inclui campos para inserir servidor SMTP, porta, usuário, senha e outros detalhes necessários.

### 3. Configuração de Template de Email
- Configuração de templates de email a partir de arquivos HTML.
- Permite criar e salvar templates reutilizáveis para envio de emails.

### 4. Envio de Emails
- Envio de emails com base em uma configuração SMTP e um template de email.
- Suporte para carregar um arquivo CSV com o padrão `carteira;email`, onde cada linha representa uma carteira e um email, separados por ponto e vírgula.
  
#### Exemplo de Formato do Arquivo CSV:
csv
carteira;email
Santander;fcarmo072@gmail.com

## Tecnologias Utilizadas
- **Backend:** Java, Spring Boot
- **Frontend:** HTML, CSS, Bootstrap, Thymeleaf
- **Banco de Dados:** SQL (com suporte a SQL Server, MySQL, etc.)

## Instalação e Configuração

### Pré-requisitos
- **Java 11+**
- **Banco de dados SQL (ex.: SQL Server)**
- **Maven**

### Passos de Instalação

1. Clone o repositório:
   bash
   git clone https://github.com/Felipe007983/ENVIADOR-DE-EMAIL-JAVA

