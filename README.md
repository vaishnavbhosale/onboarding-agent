# MerchantFlow

<div align="center">

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Deployed_on_Render-46E3B7?style=for-the-badge&logo=render&logoColor=black)

**A state-aware AI backend that guides merchants through structured onboarding — step by step, no hallucinations.**

[Live Demo](https://merchantflow-qu6i.onrender.com/) · [Swagger UI](https://merchantflow-qu6i.onrender.com/swagger-ui/index.html) · [Report Bug](https://github.com/VaishnavBhosale/MerchantFlow)

</div>

---

## 💡 Why I Built This

Most AI onboarding agents fail in production because they let the LLM decide what action to execute — leading to hallucinations, skipped steps, and unpredictable behavior.

MerchantFlow solves this with one simple principle:

> **AI handles understanding. Backend handles execution.**

The LLM classifies intent. A strict state machine enforces order. The result is a reliable, production-safe onboarding agent where a wrong action is architecturally impossible — not just unlikely.

This mirrors the problem enterprise AI faces at scale: AI agents need governed, deterministic guardrails to be trusted in production. The state machine here is that governance layer.

```
GST Verification → KYC Verification → Bank Verification → ✅ Complete
```

---

## 🧠 Architecture & Design Decisions

| Layer | Technology | Role |
|---|---|---|
| AI / NLU | LangChain4j + Gemini | Intent classification only |
| Workflow | Spring Boot State Machine | Deterministic step enforcement |
| State Store | In-Memory (Java Map) | Merchant state during session |
| API Docs | SpringDoc / Swagger UI | Interactive testing |
| Deploy | Docker + Render | Cloud hosting |

### Why AI for classification only?

- ✅ **No hallucination risk** — AI can't trigger wrong steps
- ✅ **Deterministic behavior** — backend enforces order, not the LLM
- ✅ **Easy to debug** — AI output is just a label (`GST`, `KYC`, etc.)
- ✅ **Swappable** — change LLMs without touching business logic

---

## 🔄 Onboarding State Machine

```
[STARTED] ──► [GST_VERIFIED] ──► [KYC_DONE] ──► [BANK_VERIFIED] ──► [COMPLETED]
```

| State | Description |
|---|---|
| `STARTED` | Merchant registered, onboarding begins |
| `GST_VERIFIED` | GST number validated successfully |
| `KYC_DONE` | KYC documents verified |
| `BANK_VERIFIED` | Bank account confirmed |
| `COMPLETED` | Merchant fully onboarded 🎉 |

> ⚠️ Steps cannot be skipped. Attempting KYC before GST returns a `400` with clear guidance.

---

## 🛠 Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot
- **AI Integration:** LangChain4j (Gemini / Groq)
- **State Storage:** In-memory Java Map (session-scoped)
- **API Docs:** SpringDoc OpenAPI (Swagger UI)
- **Build:** Maven
- **Deployment:** Docker + Render

---

## 📦 API Reference

### Create a Merchant

```http
POST /api/onboard
Content-Type: application/json
```

```json
{
  "name": "Test Store",
  "gst": "27ABCDE1234F1Z4"
}
```

---

### Chat with the AI Agent

```http
GET /api/chat?message=your_message
```

#### Example queries

```bash
# Verify GST
GET /api/chat?message=verify gst 27ABCDE1234F1Z4

# Verify KYC
GET /api/chat?message=verify kyc 27ABCDE1234F1Z4

# Verify Bank
GET /api/chat?message=verify bank 27ABCDE1234F1Z4

# Ask what's next
GET /api/chat?message=what next 27ABCDE1234F1Z4
```

---

### Error Response (Invalid Step Order)

```json
{
  "message": "Please complete GST verification first",
  "status": 400
}
```

---

## 🧠 "What Next?" Feature

The agent tracks each merchant's onboarding progress and tells them exactly what step comes next:

```
User:  "what next 27ABCDE1234F1Z4"
Agent: "GST verified ✅. Please proceed with KYC verification."
```

Merchant state is held in-memory during the server session. See roadmap for planned persistence upgrade.

---

## 🌍 Live Demo

| Resource | URL |
|---|---|
| 🚀 Base URL | https://merchantflow-qu6i.onrender.com/ |
| 📖 Swagger UI | https://merchantflow-qu6i.onrender.com/swagger-ui/index.html |

> **Note:** Deployed on Render's free tier. First request may take ~30 seconds due to cold start. Subsequent requests are instant.

---

## 🚀 Running Locally

### Prerequisites

- Java 17+
- Maven
- Gemini API key (get one free at [aistudio.google.com](https://aistudio.google.com))

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/VaishnavBhosale/MerchantFlow.git
cd MerchantFlow

# 2. Set your API key
export GEMINI_API_KEY=your_key_here
# or use Groq instead
export GROQ_API_KEY=your_key_here

# 3. Build and run
mvn clean package -DskipTests
java -jar target/*.jar
```

App starts at: `http://localhost:8080`

Open Swagger UI at: `http://localhost:8080/swagger-ui/index.html`

---

### 🐳 Docker

```bash
# Build image
docker build -t merchantflow .

# Run container
docker run -p 8080:8080 \
  -e GEMINI_API_KEY=your_key_here \
  merchantflow
```

---

## 🔐 Security

- API keys managed via **environment variables only**
- No secrets committed to version control
- GitHub push protection enabled

---

## 🗺 Roadmap

- [ ] **Redis integration** — persist merchant state across server restarts
- [ ] **PostgreSQL** — permanent merchant records and audit trail
- [ ] **JWT Authentication** — secure multi-tenant access so merchants only see their own data
- [ ] **Real GST/KYC API integration** — replace mock verification with actual external APIs
- [ ] **Async processing** — handle slow external verification calls without blocking
- [ ] **Conversation memory** — multi-turn chat history so the agent remembers context
- [ ] **React frontend** — dashboard for merchants to track onboarding progress

---

## 👨‍💻 Author

**Vaishnav Bhosale**

<div align="center">
<sub>Built with Java · Spring Boot · LangChain4j · Docker</sub>
</div>
