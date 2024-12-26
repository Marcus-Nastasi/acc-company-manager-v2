# Company Manager

## About

System for Managing Associations Between Companies and Suppliers

This application was developed to facilitate the management of associations between companies and their suppliers.

The application enables the viewing of companies and suppliers with pagination and search functionalities, allowing searches by name, CNPJ, and ZIP code for companies, and by name and CNPJ/CPF for suppliers.

It supports the creation, editing, and deletion of companies and suppliers, as well as the association between them, offering a user-friendly interface on the front end and a robust API on the back end.

Additionally, the API is documented with Swagger API to make it easier to understand the available routes, their payloads, and responses.

A dedicated route was also designed to provide access to the Brazilian Postal Service API.

This was a challenging project where I had the opportunity to work with the Vue.js framework for the first time.

## Entity modeling
![accenture drawio](https://github.com/user-attachments/assets/cf961050-1a82-48c4-a097-8b1cbdc9e09d)

## Technologies Used

### Front-end
- **Framework**: Vue.js
- **Language**: TypeScript
- **Styles**: Tailwind CSS and Vuetify

### Back-end
- **Framework**: Spring
- **Language**: Java
- **Tests**: JUnit and Mockito

### Database
- **DB**: PostgreSQL

### Cache
- **Cache tool**: Redis

### Documentation
- **Tool**: Swagger API

### Containerization
- **Docker** and **Docker Compose**

## How to run

Follow the steps below to configure and run the project on your local machine.

### Prerequisites

- Git
- Node.js and npm (front-end)
- java 21 (JDK) and Maven
- Docker and Docker Compose

### Steps

1. **Clone repo:**
   ```bash
   git clone https://github.com/Marcus-Nastasi/acc-company-manager
   
2. **Import front-end dependencies:**
   ```bash
   cd frontend/app
   npm install

3. **Maven's Build for back-end:**
   ```bash
   cd backend/test
   mvn clean install

4. **Run the application with Docker: Ensure you are in the root directory of the project and execute Docker Compose to bring up all services automatically:**
   ```bash
    [sudo] docker-compose up --build

5. **Wait for the build to complete and access the application: Once the build is finished, the application will be available in your browser:**
   ```bash
    http://localhost:3000/

6. **You can access the API documentation created with Swagger at the route:**
   ```bash
    http://localhost:8080/swagger-ui/index.html
