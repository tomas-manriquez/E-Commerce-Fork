# E-commerce-Fork Full Stack App

This is a full-stack e-commerce project with:
- **Frontend:** Vue.js (served via `serve`)
- **Backend:** Java Spring Boot + Maven
- **Databases:** PostgreSQL and MongoDB

---

## 🐳 Dockerized Setup

### ✅ Prerequisites

- Docker
- Docker Compose
- Node 18+ and Maven (for dev use outside containers)

---

## 🚀 Usage

### 1. Create `.env` in the root

```dotenv
DB_HOST=localhost
DB_PORT=15432
DB_MONGOPORT=27017
DB_NAME=ecommerce
DB_USERNAME=postgres
DB_PASSWORD=admin
```
### 2. Run in Development Mode
Uses volume mounts for live code reloading (hot reload):

```
docker-compose -f docker-compose.dev.yml --env-file .env up --build
```
Access:
```
Frontend: http://localhost:3000

Backend: http://localhost:8090

PostgreSQL: localhost:15432

MongoDB: localhost:27017
```

### 3. Run in Production Mode
   Builds and runs production images:

```
docker-compose -f docker-compose.prod.yml --env-file .env up --build
```

Access:
```
Frontend: http://localhost:3000 (static built app)

Backend: http://localhost:8090
```
# 🧪 Healthchecks
Backend: /actuator/health

PostgreSQL and MongoDB are monitored for readiness

# 🛠 Dev Tips
Use npm run serve locally for Vue dev (ecommerce-frontend)

Use mvn spring-boot:run for Spring Boot dev outside Docker

