
# Repsy Package Manager

## 📄 Project Overview

**Repsy Package Manager** is a minimalistic backend system designed for package management. It supports storing and retrieving packages either on a local file system or in an object storage system (MinIO / AWS S3 compatible) using a pluggable storage strategy.

The system is built with modular, clean architecture principles, following software engineering best practices and RESTful API guidelines.

---

## 🛠️ Technologies Used

- **Java 23**
- **Spring Boot 3.4.5**
- **Docker**
- **Docker Compose**
- **PostgreSQL** (Database)
- **MinIO** (Object Storage)
- **Maven** (Build Tool)

---

## Architecture Overview

- **Modular Design** with Maven modules:
  - `repsy-storage-shared`: Common interfaces
  - `repsy-storage-filesystem`: Filesystem storage implementation
  - `repsy-storage-objectstorage`: MinIO/S3 storage implementation

- **Strategy Pattern** is used to dynamically switch between storage types.
- **Persistence Layer** using JPA with PostgreSQL.
- **Containerization** using Docker and Docker Compose.

---

## 🔍 Storage Strategies

| Strategy       | Description                                         |
| -------------- | --------------------------------------------------- |
| File System Storage    | Stores packages locally inside container filesystem |
| Object Storage | Stores packages in MinIO object storage             |

Switching between strategies is **dynamic** via environment variables.

---

## 📅 Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/ziftgny/repsy-package-manager.git
cd repsy-package-manager
```

### 2. Make sure Docker and Docker Compose are installed

- [Install Docker](https://docs.docker.com/get-docker/)
- [Install Docker Compose](https://docs.docker.com/compose/install/)

### 3. Build and run using Docker Compose

```bash
docker compose up --build
```

This will:
- Start PostgreSQL database
- Start MinIO server
- Build and start the Repsy Spring Boot application

### 4. Create the MinIO Bucket
After services are started:

- Open your browser and go to http://localhost:9001 (MinIO Console)

- Login with:

  - Username: minioadmin

  - Password: minioadmin

- Click "Create Bucket +"

- Create a new bucket named exactly:

  ```
  repsy-bucket
  ```


### 5. Access points

- **Repsy App API:** `http://localhost:8080`
- **MinIO Console:** `http://localhost:9001` (Login: `minioadmin / minioadmin`)
- **PostgreSQL:** `localhost:5432`

---

## ✉️ How to Upload and Download Packages (API Guide)

###  Upload Package

- **Method:** POST
- **URL:** `http://localhost:8080/{packageName}/{version}`
- **Body Type:** form-data
- **Fields:**
  - `file`: your `package.rep` ZIP file
  - `file`: your `meta.json` file

Example:

```
POST http://localhost:8080/mypackage/1.0.0
```


###  Download Package File

- **Method:** GET
- **URL:** `http://localhost:8080/{packageName}/{version}/{fileName}`

Example:

```
GET http://localhost:8080/mypackage/1.0.0/package.rep
```


---

## 📝 How to Switch Between Storage Strategies
By default, the storage strategy is set to **Object Storage**. To switch between storage strategies, edit the `docker-compose.yml` file under the `repsy-app` environment variables section.

#### To use File System:

```yaml
storageStrategy: file-system
storage.base-path: file-storage
```

#### To use Object Storage:

```yaml
storageStrategy: object-storage
storage.endpoint-url: http://minio-container:9000
storage.access-key: minioadmin
storage.secret-key: minioadmin
storage.bucket-name: repsy-bucket
```

Restart Docker Compose:

```bash
docker compose down
docker compose up --build
```

✅ Your system will now use the selected storage strategy.

---

## 📢 API Response Codes

| Code                      | Meaning                       |
| ------------------------- | ----------------------------- |
| 200 OK                    | Operation successful          |
| 400 Bad Request           | Invalid input or file missing |
| 404 Not Found             | Package or file not found     |
| 500 Internal Server Error | Unexpected error              |

---

## 👥 Credits

Developed by: [ziftgny](https://github.com/ziftgny)

Assignment project for demonstrating:
- Software engineering best practices
- Modular backend system
- Dockerization and Deployment
