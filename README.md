#                                                           MerchantFlow 

<div align="center">

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Deployed_on_Render-46E3B7?style=for-the-badge&logo=render&logoColor=black)

**A state-aware AI backend that guides merchants through structured onboarding — step by step, no hallucinations.**

[Live Demo](https://merchantflow-qu6i.onrender.com/) · [Swagger UI](https://merchantflow-qu6i.onrender.com//swagger-ui/index.html) · [Report Bug](https://github.com/VaishnavBhosale/MerchantFlow)

</div>

---

## 📌 What is This?

Most AI agents fail in production because they let the **LLM decide what to execute** — which leads to hallucinations, skipped steps, and unpredictable behavior.

This project takes a different approach:

> **AI handles understanding. Backend handles execution.**

The LLM classifies the user's intent (`GST`, `KYC`, `BANK`, `NEXT`). A **strict state machine** enforces the correct workflow order. The result is a reliable, production-safe onboarding agent.

```
GST Verification → KYC Verification → Bank Verification → ✅ Complete
```

---

## 🧠 Architecture & Design Decisions

| Layer | Technology | Role |
|---|---|---|
| AI / NLU | LangChain4j + Gemini | Intent classification only |
| Workflow | Spring Boot State Machine | Deterministic step enforcement |
| State Store | Redis | Fast merchant state lookup |
| API Docs | SpringDoc / Swagger UI | Interactive testing |
| Deploy | Docker + Render | Cloud hosting |

### Why AI for classification only?

- ✅ **No hallucination risk** — AI can't trigger wrong steps
- ✅ **Deterministic behavior** — backend enforces order
- ✅ **Easy to debug** — AI output is just a label (`GST`, `KYC`, etc.)
- ✅ **Scalable** — swap LLMs without changing business logic

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

## 🧠 Memory & "What Next?" Feature

The agent tracks each merchant's progress across all three steps:

```
User:  "what next 27ABCDE1234F1Z4"
Agent: "GST verified ✅. Please proceed with KYC verification."
```

State is stored in **Redis** for fast, reliable lookups — even across restarts.



## 🌍 Live Demo

| Resource | URL |
|---|---|
| 🚀 Base URL | https://merchantflow-qu6i.onrender.com/ |
| 📖 Swagger UI | https://merchantflow-qu6i.onrender.com/swagger-ui/index.html |

> **Note:** Deployed on Render's free tier — may take ~30 seconds to wake up on first request.



## 🚀 Running Locally

### Prerequisites

- Java 17+
- Maven
- Gemini API key

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/VaishnavBhosale/MerchantFlow.git
cd onboarding-agent

# 2. Set environment variables
export GEMINI_API_KEY=your_key_here
# or
export GROQ_API_KEY=your_key_here

# 3. Build and run
mvn clean package -DskipTests
java -jar target/*.jar
```

App starts at: `http://localhost:8080`


### 🐳 Docker

```bash
# Build image
docker build -t MerchantFlow .

# Run container
docker run -p 8080:8080 \
  -e GEMINI_API_KEY=your_key_here \
  onboarding-agent
```



## 🔐 Security

- API keys managed via **environment variables only**
- No secrets committed to version control
- GitHub push protection enabled



## 🗺 Roadmap

- [ ] JWT Authentication
- [ ] PostgreSQL for persistent merchant data
- [ ] Multi-merchant concurrent onboarding
- [ ] Async / event-driven processing
- [ ] Conversation memory (multi-turn chat history)
- [ ] React frontend dashboard



## 👨‍💻 Author

**Vaishnav Bhosale**



<div align="center">
<sub>Built with Java · Spring Boot · LangChain4j · Redis · Docker</sub>
</div>
