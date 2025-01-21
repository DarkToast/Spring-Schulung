# Spring Boot Schulung

### Übungsbeispiel: Archiv-Verwaltung

Zum Erklären und Lernen der Spring Grundlagen und Konzepte soll ein eigener Server von Grund auf aufgesetzt und
implementiert werden.

### Grundlagen:

- Spring Basics
- Annotations
- Dependency Injection
- Lifecycles
- Architektur
- Unit- vs. Component-Test

### Ablauf:

Neues Projekt anlegen mit:

- Spring Boot
- Kotlin
- Temurin 21
- DevTools
- starter-web
- H2 Datenbank

(Projekt-spezifische maven config hinzufügen)

#### 1. Objekt: Die Applikation

- Application starten

#### 2. Der Controller

- Einen @Controller erstellen
  (oder @RestController, wenn spring-boot-starter-web vorhanden)
- BookController getBooks
- http-client oder Browser für den Aufruf verwenden

#### 3. Der Service

- Einen GET für eine Liste von Books erstellen und zurückgeben.
  (Entweder per Hand coden oder aus CSV-Liste einlesen)
- Einen POST für das Hinzufügen einzelner Books (saveBook)

#### 4. Der Test

- SpringBootTest & AutoConfigureMockMvc
- mockMvc.perform(…).andExpect(…)
- MockBeans: MockK statt Mockito

#### 5. Die Datenbank

- Konfiguration (yaml) anpassen (Schema Generierung fürs erste)
- spring-boot-starter-data-jpa
- org.jetbrains.kotlin.plugin.jpa

- Book zu Entity machen (gleichzeitig JSON & DB Objekt) mit @Id id: Long, nullables, uniques
- Repository: JpaRepository

mögliche Probleme: reserved commands als identifier (Bsp. "year")

Offen: DB Navigator oder Web-view für das DB-Schema

Befüllen der Initialdaten (z.B. per Webb Client in test)

#### 6. Erweitern des Controllers:

- Filter-Parameter
- Suche, etc.

#### 7. Erweitern des DB-Schemas:

- Aufsplitten von Title, Author, Publisher in getrennte Tabellen
- Befüllen der Testdaten aus einer Datei statt in-Memory Generierung