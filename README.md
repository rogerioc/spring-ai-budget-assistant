# 🎙️ Spring AI Budget Assistant (BudgetinAI)

> **Assistente financeiro inteligente que transforma comandos de voz naturais em transações financeiras estruturadas, utilizando Spring AI, OpenAI, Ollama, Spring Security (JWT), Persistência Poliglota (MySQL + MongoDB) e Arquitetura Limpa.**

[![Java 25](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x%2F4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-2.0.2-blue.svg)](https://spring.io/projects/spring-ai)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT%20Stateless-red.svg)](https://spring.io/projects/spring-security)
[![MySQL](https://img.shields.io/badge/MySQL-8.x%20ACID-blue.svg)](https://www.mysql.com/)
[![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL%20Metadata-green.svg)](https://www.mongodb.com/)
[![Ollama](https://img.shields.io/badge/Ollama-Local%20LLM-black.svg)](https://ollama.ai)
[![DIO Track](https://img.shields.io/badge/DIO-Spring%20AI%20Track-E91E63.svg)](https://github.com/digitalinnovationone/dio-spring-boot-learning-track/blob/main/05-spring-ai/README.md)
[![Gradle](https://img.shields.io/badge/Gradle-9.x-02303A.svg)](https://gradle.org)
[![Docker](https://img.shields.io/badge/Docker%20Compose-MySQL%20%2B%20Mongo-2496ED.svg)](https://www.docker.com/)

---

## 💡 Visão Geral

O **BudgetinAI** elimina a fricção da digitação manual de despesas. Em vez de preencher formulários com datas, categorias e valores, o usuário apenas envia um áudio (ex: *"Gastei 50 reais no mercado"* ou *"Abasteci 120 reais no posto"*).

O sistema processa a fala com modelos de ponta, compreende a intenção através de **Tool Calling**, persiste os dados de forma estruturada no banco relacional, audita os metadados do processamento de IA no MongoDB e responde em texto ou áudio sintetizado.

<p align="center">
  <img src="doc/visao-geral.png" alt="O Assistente de Budgeting: Transformando Voz em Dados Financeiros" width="850"/>
</p>

---

## 🚀 A Evolução das Interfaces

Interfaces clássicas dependem de formulários estáticos e validações rígidas de JSON. A nova era de APIs com IA traz interfaces flexíveis, tolerantes e guiadas por intenção de voz:

| Padrão Clássico (CRUD) | Nova Era com IA (BudgetinAI) |
| :--- | :--- |
| Entrada manual de campos | Entrada por voz em linguagem natural |
| JSON estático e rígido | Extração semântica com Whisper + LLM |
| Múltiplos cliques no app | Registro instantâneo por comando de voz |
| Resposta puramente textual | Resposta multimodal (JSON, Texto ou Áudio TTS) |
| Logs descartados ou em texto | Metadados e auditoria completa da IA em MongoDB |

<p align="center">
  <img src="doc/evolucao-interfaces.png" alt="A Evolução das Interfaces" width="850"/>
</p>

---

## 🔄 Fluxo de Interação & Persistência Poliglota

```mermaid
flowchart TD
    Client["📱 Cliente / REST"] -->|1. Envia Áudio com Bearer Token| API["🚀 Spring Boot API"]
    
    subgraph StorageLayer ["Armazenamento Físico"]
        Storage[("📁 Local Storage / S3<br/>grava binário particionado por data")]
    end
    
    subgraph MongoLayer ["NoSQL (Auditoria & Metadados)"]
        MongoDB[("🍃 MongoDB (budgetin_files)<br/>status, caminho, transcrição, resposta IA")]
    end
    
    subgraph SQLLayer ["Relacional ACID"]
        MySQL[("🐬 MySQL (transaction)<br/>transações, usuários, owner UUID")]
    end
    
    API -->|Salva binário físico| Storage
    API -->|Registra status RECEIVED| MongoDB
    API -->|2. Whisper STT| Whisper["🎙️ OpenAI Whisper"]
    Whisper -->|Texto transcrito| API
    API -->|Atualiza status TRANSCRIBED| MongoDB
    API -->|3. Raciocínio & Tool Calling| LLM["🤖 Llama 3.2 (Ollama) / GPT-4o-mini"]
    LLM -->|Invoca Tool: persistTransaction| API
    API -->|4. Grava transação com Owner UUID| MySQL
    API -->|Atualiza status PROCESSED| MongoDB
    API -->|5. OpenAI TTS| TTS["🔊 Síntese de Voz (MP3)"]
    TTS -->|Retorna Áudio MP3| Client
```

1. **Entrada de Áudio Autenticada:** O usuário envia uma mensagem de voz (`.m4a`, `.mp3`, `.wav`) com token JWT.
2. **Armazenamento Físico:** O binário é gravado em disco organizado por diretórios de data (`./storage/uploads/YYYY-MM-DD/`).
3. **Metadados no MongoDB:** Um documento em `uploaded_files` é criado com status `RECEIVED`, registrando metadados técnicos do arquivo.
4. **Speech-to-Text (STT):** O **Whisper** da OpenAI transcreve o áudio e o texto é auditado no MongoDB (`TRANSCRIBED`).
5. **Compreensão e Tool Calling:** O **Llama 3.2 (local via Ollama)** ou **GPT-4o-mini** extrai valor, categoria e descrição, chamando a `@Tool` `persist-transaction`.
6. **Persistência Relacional (MySQL):** A transação financeira é salva no MySQL com o UUID do usuário autenticado no objeto `owner`.
7. **Conclusão & TTS:** O status no MongoDB é atualizado para `PROCESSED` e a resposta falada é sintetizada em MP3 para retorno ao usuário.

---

## 🏛️ Arquitetura do Projeto (Clean Architecture & DDD)

O projeto é organizado por **Domínios/Contextos Delimitados (Package by Feature)** com total desacoplamento entre módulos:

```text
src/main/java/org/rogeriocs/budgetinai/
├── BudgetinAiApplication.java
│
├── auth/                                     # Domínio: Identidade & Segurança (JWT Stateless)
│   ├── domain/                               # Entidades e Value Objects
│   │   └── UserRole.java                     (ROLE_USER, ROLE_ADMIN)
│   └── infrastructure/                       # Adaptadores Spring Security & JPA
│       ├── http/ (AuthController - Login e Registro)
│       ├── persistence/                      # User JPA Entity (UUID VARCHAR(36)), UserRepository
│       └── security/                         # SecurityConfig, JwtService, JwtAuthenticationFilter, JpaUserDetailsService
│
├── transaction/                              # Domínio: Gestão Financeira
│   ├── domain/                               # Entidades e regras puras (Zero acoplamento com auth)
│   │   ├── Transaction.java
│   │   ├── TransactionId.java
│   │   ├── Category.java                     (GROCERIES, PHARMA, AUTO)
│   │   ├── Owner.java                        # Record Owner(UUID id, String name)
│   │   └── TransactionRepository.java        (Porta de Saída)
│   ├── application/                          # Casos de uso
│   │   ├── PersistTransactionUseCase.java
│   │   ├── ListTransactionsByCategoryUseCase.java
│   │   ├── input/PersistTransactionInput.java
│   │   └── output/TransactionOutput.java
│   └── infrastructure/                       # Adaptadores JPA e HTTP
│       ├── http/                             # TransactionController, TransactionRequest, TransactionResponse
│       └── persistence/                      # TransactionEntity (owner_id VARCHAR(36)), JpaTransactionRepository
│
├── file/                                     # Domínio: Arquivos & Metadados de IA
│   ├── domain/                               # Entidades e Portas
│   │   ├── FileMetadata.java                 # Ciclo de vida: RECEIVED, TRANSCRIBED, PROCESSED, FAILED
│   │   ├── FileProcessingStatus.java
│   │   ├── FileMetadataRepository.java       (Porta para NoSQL)
│   │   └── FileStorageService.java           (Porta para Storage físico)
│   └── infrastructure/                       # Adaptadores MongoDB e Storage
│       ├── http/ (FileMetadataController - Consulta de histórico de IA)
│       ├── persistence/mongo/                # FileMetadataDocument, MongoFileMetadataRepository, MongoConfig (UUID Standard)
│       └── storage/                          # LocalFileStorageService (particionamento em disco)
│
└── assistant/                                # Domínio: Assistente de Inteligência Artificial
    ├── domain/                               # Portas puras (independente de IA específica)
    │   ├── AudioTranscriber.java
    │   ├── FinancialAssistant.java
    │   ├── VoiceSynthesizer.java
    │   └── ChatAssistant.java
    ├── application/                          # Casos de uso de orquestração
    │   ├── ProcessVoiceTransactionTextUseCase.java
    │   ├── ProcessVoiceTransactionAudioUseCase.java
    │   ├── TranscribeAudioUseCase.java
    │   ├── SynthesizeVoiceUseCase.java
    │   └── ChatUseCase.java
    └── infrastructure/                       # Adaptadores de Framework
        ├── springai/                         # TransactionTools (@Tool isoladas), SpringAiFinancialAssistant, etc.
        ├── ollama/                           # OllamaFinancialAssistant, OllamaChatAssistant (Local LLM)
        └── http/                             # TransactionAssistantController, ChatController, etc.
```

---

## 📡 Endpoints da API

### 1. Autenticação (Público)
| Método | Endpoint | Descrição | Consome | Retorna |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Cadastro de novo usuário | `application/json` | `AuthResponse` (Token JWT) |
| `POST` | `/api/auth/login` | Login com usuário e senha | `application/json` | `AuthResponse` (Token JWT) |

### 2. Assistente de Voz & Transações (Requer Bearer Token)
| Método | Endpoint | Descrição | Consome | Retorna |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/transactions/ai/audio` | Processa áudio, grava no MySQL/Mongo e devolve áudio MP3 | `multipart/form-data` | `audio/mp3` |
| `POST` | `/transactions/ai/text` | Processa áudio, grava no MySQL/Mongo e devolve texto | `multipart/form-data` | `text/plain` |
| `GET` | `/transactions/ai/files` | Histórico e auditoria de arquivos/IA gravados no MongoDB | - | `List<FileMetadata>` |
| `POST` | `/transactions` | Criação manual de transação financeira | `application/json` | `TransactionResponse` |
| `GET` | `/transactions/{category}` | Lista transações por categoria (`GROCERIES`, `PHARMA`, `AUTO`) | - | `List<TransactionResponse>` |

### 3. Serviços Básicos de IA
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/transcribe` | Transcreve arquivo de áudio para texto |
| `POST` | `/api/synthesize` | Sintetiza texto enviado em arquivo de áudio MP3 |
| `GET` | `/api/chat` | Chat interativo simples com o assistente via query param `?prompts=` |
| `GET` | `/api/chat-model` | Chamada direta ao modelo de chat |

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 25 (OpenJDK Temurin)
* **Framework:** Spring Boot & Spring Data JPA & Spring Data MongoDB
* **Segurança:** Spring Security com autenticação Stateless via Bearer JWT (`jjwt 0.12.6`)
* **Framework de IA:** Spring AI (OpenAI Starter, Ollama Starter & Tools API)
* **Modelos de IA Suportados:**
  * **Nuvem (OpenAI):** Whisper (`whisper-1`), GPT-4o-mini e TTS (`gpt-4o-mini-tts`)
  * **Local (Ollama):** Llama 3.2 com execução 100% local, gratuita e privada para Tool Calling
* **Bancos de Dados:**
  * **MySQL 8:** Persistência relacional ACID com UUIDs mapeados como `VARCHAR(36)` legíveis
  * **MongoDB:** Persistência de documentos NoSQL para metadados de arquivos e logs de IA
* **Build & Testes:** Gradle (Kotlin DSL), JUnit 5, Mockito e AssertJ

---

## 🚀 Como Executar Localmente

### 1. Pré-requisitos
* **Java 25** instalado
* **Docker** e **Docker Compose**
* Chave de API da **OpenAI** (para Whisper/TTS) e/ou **Ollama** rodando localmente

### 2. Iniciar os Containers (MySQL + MongoDB + Ollama)
```bash
docker compose up -d
```
* **MySQL:** Porta `3307` (banco: `transaction`, usuário: `app/app`)
* **MongoDB:** Porta `27017` (banco: `budgetin_files`, usuário: `root/root`)
* **Ollama:** Porta `11434`

### 3. Configuração Híbrida (`application.properties`)
```properties
# Provedores de IA Desacoplados
app.ai.provider.llm=ollama     # 'ollama' (privacidade local) ou 'openai'
app.ai.provider.stt=openai     # 'openai' (Whisper)
app.ai.provider.tts=openai     # 'openai' (TTS)

# Configurações MongoDB
spring.data.mongodb.uri=mongodb://root:root@localhost:27017/budgetin_files?authSource=admin
spring.data.mongodb.uuid-representation=standard
app.storage.location=./storage/uploads
```

### 4. Executar a Aplicação
```bash
export $(grep -v '^#' .env | xargs) && ./gradlew bootRun
```

### 5. Executar os Testes Automatizados
```bash
./gradlew test
```

---

## 🧪 Validação Rápida via REST Client

O projeto conta com o arquivo **[`restfiles/AUTH.http`](file:///home/rogerio/Development/Bootcamps/DioJavaIA/budgetinAI/restfiles/AUTH.http)** pronto para execução:
1. **Passo 1:** Testa a proteção da API sem token (`403 Forbidden`).
2. **Passo 2:** Autentica com o usuário padrão (`rogerio` / `password`) e captura o JWT.
3. **Passo 3 & 4:** Consulta e cria transações vinculadas ao UUID do usuário autenticado.
4. **Passo 6:** Envia áudio de voz para a IA transcrever, classificar, persistir no MySQL e responder em MP3.
5. **Passo 7:** Consulta o MongoDB (`GET /transactions/ai/files`) comprovando a auditoria e metadados persistidos.

---

## 🗺️ Roadmap de Evolução

- [x] Estruturação com Clean Architecture modular por domínios
- [x] Transcrição com Whisper e Tool Calling com Spring AI
- [x] Suporte a LLM local gratuito com Ollama (Llama 3.2)
- [x] Autenticação Stateless com Spring Security e JWT
- [x] Mapeamento legível de UUIDs como `VARCHAR(36)` no MySQL
- [x] Desacoplamento entre Bounded Contexts com `Owner(UUID, String)`
- [x] Persistência Poliglota: MongoDB para metadados de arquivos e auditoria de IA
- [x] Armazenamento físico de áudios particionado por data
- [ ] Suporte a múltiplos provedores em nuvem (Claude / Gemini)
- [ ] Processamento assíncrono de áudio com mensageria (Kafka / RabbitMQ)
- [ ] Relatórios e dashboards de gastos por período e categoria

---

## 📄 Licença

Este projeto é desenvolvido para fins de estudo e aprimoramento em arquitetura de software e inteligência artificial aplicada.
