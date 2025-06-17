# Sistema de Gestão de Tarefas

> ✅ Projeto em produção:  
> 🌐 <https://esig-gloria.up.railway.app/>

Este projeto é uma aplicação web para um sistema de gestão de tarefas, desenvolvida como parte do processo seletivo da *
*ESIG Group** para a vaga de estágio em **Desenvolvimento Java Web**.

O objetivo deste projeto é demonstrar competências técnicas na construção de uma aplicação CRUD completa utilizando
JavaServer Faces (JSF), com persistência em banco de dados PostgreSQL e deploy via Docker em ambiente cloud (Railway).

---

## 📋 Orientações Atendidas (ESIG Group)

| Item | Descrição                                                               | Status     |
|------|-------------------------------------------------------------------------|------------|
| a)   | Aplicação Java Web utilizando **JavaServer Faces (JSF)**                | ✅ Feito    |
| b)   | Persistência com **PostgreSQL**                                         | ✅ Feito    |
| c)   | Uso de **JPA** com Hibernate                                            | ✅ Feito    |
| d)   | Deploy em ambiente **Cloud (Railway)**                                  | ✅ Feito    |
| e)   | Outros diferenciais: uso de **Docker Compose**, **CDI**, **PrimeFaces** | ✅ Incluído |

---

## 🧠 Sobre o Projeto

O objetivo foi desenvolver uma aplicação CRUD completa de gestão de tarefas, com foco em:

- Persistência em banco de dados com JPA
- Interface com JSF e PrimeFaces
- Filtros para facilitar busca de tarefas
- Deploy real em ambiente cloud (Railway)

---

## ✅ Funcionalidades Implementadas

- **Criação de Tarefas** com os campos:
    - Título
    - Descrição
    - Responsável
    - Prioridade (ALTA, MEDIA, BAIXA)
    - Deadline
    - Situação (inicialmente como "PENDENTE")

- **Listagem de Tarefas**
- **Filtro de Tarefas** por:
    - Número (ID)
    - Título ou Descrição
    - Responsável
    - Situação (EM_ANDAMENTO / CONCLUIDA)

- **Edição de Tarefas**
- **Remoção de Tarefas**
- **Conclusão de Tarefas**

---

## 🛠️ Tecnologias Utilizadas

- **Java 11**
- **Framework Web: JavaServer Faces (JSF 2.3)**
    - Implementação JSF: Mojarra 2.3.2
    - FacesConverter: Utilizado para realizar a conversão de dados entre a camada de apresentação e a camada de modelo.
- **Apache Maven 3.9.9**
- **JPA (Hibernate 5.6.15.Final)**
- **PostgreSQL 14**
- **Apache Tomcat 9.0.99**
- **CDI (Weld 3.1.9.Final)**
- **PrimeFaces 10.0.0**
- **OmniFaces 3.14**
- **Hibernate Validator 6.1.7.Final**
- **Docker + Docker Compose**
- **Deploy Cloud com Railway.app**

---

## 🧪 Execução da Aplicação

### Opção 1: Docker Compose (Desenvolvimento Recomendado)

> 💡 O banco de dados e a aplicação são executados como contêineres separados.

```bash
git clone https://github.com/gloria-mariass/esig-tarefas.git
cd esig-tarefas
docker compose up --build -d
```

**Acesse a aplicação em:**  
[http://localhost:8080](http://localhost:8080)

---

### 🔹 Opção 2: Deploy em Cloud (Railway)

- PostgreSQL provisionado via Railway.app
- `.war` gerado localmente e incluído no repositório (ver `.gitignore`)
- Variável de ambiente `DATABASE_URL` configurada automaticamente pelo Railway

**Acesse o sistema em produção:**  
🌐 [https://esig-gloria.up.railway.app/](https://esig-gloria.up.railway.app/)

---

## 📂 Estrutura do Projeto

```plaintext
gestao-tarefas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/esig/desafio/gestaotarefas/
│   │   │       ├── controller/         # Managed Beans JSF
│   │   │       ├── converters/         # Conversores JSF
│   │   │       ├── dao/                # Acesso a dados com JPA
│   │   │       ├── modelo/             # Entidades JPA
│   │   │       └── util/               # Utilitários
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml     # Configuração do JPA
│   │   └── webapp/
│   │       ├── META-INF/               # Metadados JSF (padrão do Java EE)
│   │       ├── resources/              # Recursos estáticos (CSS)
│   │       ├── WEB-INF/
│   │       │   ├── includes/           # Fragmentos de página (ex: modais)
│   │       │   └── lib/                # Dependências externas
│   │       ├── cadastro_tarefas.xhtml  # Página de criação/edição de tarefas
│   │       ├── listagem.xhtml          # Página de listagem e filtros de tarefas
│   │       ├── beans.xml               # Configuração do CDI
│   │       └── web.xml                 # Configuração da aplicação web
├── Dockerfile                          # Criação da imagem da aplicação
├── docker-compose.yml                  # Sobe o app Java + PostgreSQL em containers separados e integrados
└── pom.xml                             # Gerenciamento de dependências e build (Maven)

