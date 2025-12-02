# ProChat

```contributors
Tony Karlin, Onni Kivinen, Joni Heikkilä, Jarkko Kärki
```

## Description

Our project is designed to help users easily add contacts and chat with them.

## Features

- **Client (JavaFX)**

  - Create users
  - Add users as contacts
  - Create conversations
  - Chat with your contacts
  - Change language (EN, SWE, FI, JP, RU)

- **Backend (Java / Spring Boot)**
  - User authentication and authorization
  - Manage user profiles and contacts
  - Create and manage conversations
  - Store and retrieve chat messages
  - Real-time message handling (to be implemented in the future)

## Rough Project Structure

```bash
Backend/                # Java Spring Boot backend
├─ database/            # Database initialization
├─ http-requests/       # HTTP request snippets for manual testing - Rest files
├─ src/
│ ├─ main/
│ │ ├─ java/
│ │ │ └─ backend_api/
│ │ │  ├─ controller/    # REST & WebSocket controllers
│ │ │  ├─ dto/           # Data Transfer Objects for users, messages, conversations
│ │ │  ├─ entities/      # Database entities
│ │ │  ├─ repository/    # Spring Data repositories
│ │ │  ├─ services/      # Business logic services
│ │ │  └─ utils/         # Utilities (JWT, exception handling, etc.)
│ │ └─ resources/
│ │ └─ application.properties
│ └─ test/               # Unit and integration tests
├─ Dockerfile
├─ pom.xml
└─ sonar-project.properties
```

```bash
Frontend/               # JavaFX client
├─ src/
│ ├─ main/
│ │ ├─ java/
│ │ │ ├─ application/   # Main.java
│ │ │ ├─ controller/    # UI controllers
│ │ │ ├─ callback/      # Callbacks (contactslist & language updating)
│ │ │ ├─ model/         # Domain models (User, Message, Contact, Conversation)
│ │ │ ├─ service/       # API clients to interact with backend
│ │ │ ├─ utils/         # API url, handlers, utilities
│ │ │ └─ view/          # JavaFX view class
│ │ └─ resources/       # FXML files, CSS, images
│ └─ test/              # Client tests
├─ Dockerfile
├─ pom.xml
└─ sonar-project.properties
```

```bash
docs/                   # Project documentation (plans, diagrams, sprint reports)
├─ diagrams/
├─ project-initialization/
├─ sonarqube-results/
├─ sprint-documentation/
└─ testing/
```

## Frontend

The frontend is built with Java and JavaFX that allows users to communicate with their contacts.

### Supported Languages

- English (en)
- Finnish (fi)
- Russian (ru)
- Swedish (sv)
- Japanese (ja)

#### Language Selection

In the client:

1. Open language selector from the top-left corner
2. Choose your preferred language
3. The interface updates instantly without restarting the client

#### File Structure

```bash
Frontend/
 └─ src/
    └─ main/
       └─ resources/
          └─ localization/
            LanguageBundle_en_US.properties
            LanguageBundle_fi_FI.properties
            LanguageBundle_ja_JP.properties
            LanguageBundle_ru_RU.properties
            LanguageBundle_sv_SE.properties
```

Each `.properties` file contains translated key-value pairs.

#### Localization Resources

- **Translators:** Initial translations for Finnish, Russian, Swedish, and Japanese were created using **AI tools**.
- **Localization Tools:** Translations were handled manually during Sprint 5 using `.properties` files in the `Frontend/src/main/resources/localization/` directory.
- **Encoding:** All localization files use **UTF-8** encoding to ensure correct rendering of non-Latin characters.

#### Developers

##### Adding a new language

1. Right-click `LanguageBundle` inside localization folder to add a new property file.
2. Name it using the language and country code, e.g. `_fr_FR`.
3. Copy all the keys from existing file (e.g. `LanguageBundle_en_US.properties`) and translate the values.
4. Add the new language option to the **LanguageController** class: `src/main/java/controller/component/LanguageButtonController.java` (around line 59).
5. Test that all UI components update dynamically and text renders correctly.

> Make sure all `.properties` files are saved in **UTF-8** encoding to support non-Latin characters

## Backend

The backend of the project is built with Java and Spring Boot, using Maven as the build tool. It handles the business logic and data storage, using PostgreSQL as the primary database, and provides a RESTful API that the frontend client uses to make HTTP requests.

## Diagrams

Diagrams can be found in the [docs/diagrams](docs/diagrams) folder.

- ER-diagram
- Relational-schema
- Usecase-diagram
- Activity-diagram
- Class-diagram(s)
- Sequence-diagram
- Deployment-diagram(s)

`Production` deployment diagram.

![production](docs/diagrams/09-project-deployment-diagram-production.png)

`Development` deployment diagram.

![development](docs/diagrams/08-project-deployment-diagram-development.png)

## Jenkins CI/CD

Jenkins is currently handled locally since the backend has not yet been integrated into a remote server, which we believe will be implemented in the next course.

### General flow of the pipeline

```mermaid
flowchart TD
  subgraph "Jenkins Pipeline"
    A[Checkout Source Code] --> B[Build Backend]
    B --> C[Start Backend]
    C --> D[Build Frontend]
    D --> E[Run Frontend & Backend Tests]
    E --> F[Generate Code Coverage Reports]
    F --> G[Publish Test Results]
    G --> H[Archive Coverage Reports]
    H --> I[Build & Push Docker Images]
    I --> J[Deploy with Docker Compose]
  end
```

Currently there are three docker images for the project. One for the Frontend (Client), one for the Backend (Springboot Server) and one for the database (PostgreSQL).

## Testing

Testing is performed on both the backend and frontend components of the project.

### Backend Testing

- **Unit Tests:** The backend uses JUnit and Mockito to test individual services, controllers, and repositories. These tests verify business logic, data validation, and API endpoints.
- **Integration Tests:** Integration tests ensure that different modules work together, including database interactions and REST API responses.
- **Test Coverage:** Code coverage is measured using tools like JaCoCo, and reports are generated in the CI/CD pipeline.

### Frontend Testing

- **Unit Tests:** The frontend includes JUnit-based tests for controllers and models to validate UI logic and data handling.
- **Manual Testing:** JavaFX UI components are manually tested to ensure correct user interaction and display.
- **End-to-End Testing:** Basic end-to-end scenarios are tested by running the client against the backend to verify user flows such as login, contact management, and messaging.

All tests are executed automatically in the Jenkins pipeline, with results and coverage reports archived for review.

## Docker Image

```yaml
docker-compose up -d
```

## SonarQube

SonarQube analysis for both Backend and Frontend.

```cmd
cd .\Backend\
sonar-scanner
```

```cmd
cd .\Frontend\
sonar-scanner
```

## Future development

- Implement live chatting and possible voice calls.
- Public walls where any user can join by a specific topic and chat about.
- Calendar for planning events and meetings.
- Pinning messages.
- Live Notifications for alerts etc.
