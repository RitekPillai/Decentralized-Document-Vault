# Decentralized Document Vault

> The Decentralized Document Vault is an API for uploading sensitive files securely, managing access controls, and generating expiring shareable links.

## How it works

1. The user uploads a file via the frontend UI or a `curl` command.
2. The backend encrypts the data using AES/RSA cryptographic algorithms.
3. The encrypted bytes are streamed and saved into cloud storage.

## Technologies Used

* **Backend:** Spring Boot
* **Database:** PostgreSQL (for storing file metadata)
* **Cloud Storage:** Azure Blob Storage
* **DevOps:** Docker & GitHub Actions (CI/CD)

## Prerequisites

* **Docker**
* **Java >= 17**
* **Git**

## Quick Start

Make sure you have Docker installed. Run the following command to download the required images and start the containers:
```bash
docker compose up -d
```
Move into the backend folder, either by navigating through your terminal or opening the folder directly in your IDE:

```
cd backend
```
Enter this Commnd to run the Spring 

```
mvn spring-boot:run
```

(Note: If that command does not work, try ./mvnw spring-boot:run. Ensure you are inside the backend folder before running it.)



## Directory Structure

```
📁 document-vault
├── 📁 backend/                # Spring Boot REST API
├── 📁 frontend/               # Angular User Interface
└── 📁 documentation/          # Architecture and API Specs
    ├── introduction.md
    ├── architecture.md
    └── api-spec.md
```
This is the directory structure of the project. Each tool and framework is separated according to its purpose.

Check out the [Architecture Documentation](Architecture.md) to see the full system design of this project or Check [Api contract](API_Contract.md) to see the APi design of it .