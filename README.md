# Spring Boot Schulung

## Übungsbeispiel: Buch-Verwaltung

Zum Erklären und Lernen der Spring Grundlagen und Konzepte soll ein eigener Server von Grund auf aufgesetzt und
implementiert werden.

## Grundlagen:

- Spring Basics
- Annotations
- Dependency Injection
- Lifecycles
- Architektur
- Unit- vs. Component-Test

## Ablauf:

Neues Projekt anlegen mit:

- Spring Boot
- Kotlin
- Temurin JDK 21
- DevTools
- starter-web
- H2 Datenbank

## Grundübung

In der Grundübung soll ein einfacher REST-Service erstellt werden, der eine Liste von Büchern verwaltet.
Ein Buch kann per CRUD angelegt, gelöscht, gelesen und geändert werden.

Ein Buch hat folgende Eigenschaften:
+ `id: Long`
+ `author: String`
+ `title: String`
+ `publisher: String`
+ `year: Int`
+ `ean: String`

Per API sollen Abfragen auf die Bücher und eine Liste der Bücher möglich sein:
* Anlegen eines neuen Buchs
* Abfrage eines bestehenden Buchs
* Abfrage aller Bücher
* Die Bücher eines Autors
* Die Bücher eines Jahres
* Die Bücher, welche älter als N Jahren sind.
* Eine Kombination aus zwei Abfragen. 

### 1. Die Applikation
- Application starten
- Die erste Hello World Ausgabe mit Spring Bean.
- Grundlagen der Dependency Injection

### 2. Die API
- Anhand der oben beschrieben Operationen eine REST API designen und skizzieren.
  z.B. per .http-File oder im Postman oder vergleichbaren Tools.
  - API Design als Top-Down Ansatz.

### 3. Der Controller
- Einen Controller per Annotations erstellen
  (@Controller oder @RestController, wenn spring-boot-starter-web vorhanden)
  - weiter Grundlagen der Dependency Injection
- Den Controller um die oben beschrieben und als API designten Operationen
  für Bücher erstellen und die API umsetzen.
  - Verständnis des Spring Routings.
  - Vorerst nur mit Dummy-Daten.
- http-client oder Browser für den Aufruf verwenden
  - Postman oder besser IntelliJ Ultimate
  - Verständnis für HTTP Request und Response Calls

### 4. Validierung
- Grundlagen der Jakarta Validation Annotations
- Grundlagen der Spring Implementierung
  - Annotations
  - eigene Cross-Fields Validierungen
  - Spring Web Exception-Handling
  - Fehlerverhalten auf API Ebene

### 5. Der Test
- Einführung von SpringBootTest
- Erster MVC Test gegen die API
  - Einzelnes Buch
  - Liste von Büchern
  - restliche API Calls 
- Wiederholung der Testpyramide.
  - Unterscheidung Unit und Componenttest
- Hints:
  - SpringBootTest & AutoConfigureMockMvc
  - mockMvc.perform(…).andExpect(…)
  - MockitoBean: Mockito

### 6. Die Datenbank
- H2 Datenbank als Testdatenbank hinzufügen
- Konfiguration, um das Schema generieren zu lassen.
- Hints:
  - spring-boot-starter-data-jpa
  - org.jetbrains.kotlin.plugin.jpa
- Die Klasse Book zur Entity umbauen.
  - JpaRepository, @Id, @Entity, nullables, uniques.
  - mögliche Probleme: reserved commands als identifier (Bsp. "year")

### 7. Erweitern des Modells um Relationen
- Erweiterung um Authoren und Publisher Informationen.
  - 1 Buch hat 1 Autor und 1 Publisher.
  - 1 Author hat n Bücher
  - 1 Publisher hat n Bücher
  - Einführung von OneToMany und OneToOne Relationen.
  - Fremdschlüssel
  - Abfragen mit JPA
- Erweitern der API um die neuen Informationen
  - CRUD Operationen für Author und Publisher
  - Erweitern der Suche um Autoren und Publisher.
  - API und Architekturüberlegungen. 
    - Resource bei REST vs. Entity im Modell.
    - Gemeinsame genutzte Codeteile.
    - Abstraktion vs. Delegation

### 8. Absicherung der API durch eine Benutzerverwaltung
- Grundlagen OAuth2
- Grundlagen Spring Security
- Anlage einer kleinen In-Memory Userdatenbank.
- Absichern der API anhand eines Rollenkonzepts.
- Einarbeitung der Sicherheitsschicht in die Tests.

## Zusatzaufgaben I
- Optimierung des Modells
  - Welche Teile können zusammen gezogen werden.
  - Welche Elemente brauchen eigene Objekte. (Entity vs. ValueObject)
  - Welche Elemente brauchen eigene Tabellen? (@OneToOne vs. @Embedded)
- Erweitern der Relationen
  - Ein Buch kann n Autoren haben. Mindestens einen.
  - Ein Buch kann nur einen Publisher haben.
- Erweitern der API
  - CRUD für Autoren
  - CRUD für Publisher
  - Zuweisen von Autoren und Publishern bei der Anlage von Büchern.
    - Fremdrelationen. IDs als Fremdschlüssen.
- Erweitern der Tests
  - Erweitern der Tests, um die neuen API Endpunkte.
  - Überlegung: Wo braucht es ComponentTests, wo UnitTests?

## Zusatzaufgaben II
- Weitere Relationen
  - Ein Publisher hat n Autoren.
  - Ein Autor ist unter n Publishern unter Vertrag.
  - Alle Autoren eines Buches müssen unter demselben Publisher unter Vertrag sein.
  - ManyToMany Relationen
- Erweitern der API
  - Zuweisung von Autoren zu einem Publisher bei der Anlage.
  - Plausibilitätsprüfung bei der Anlage von Büchern.
- Erweitern der Tests
  - Erweitern der Tests, um die neuen API Endpunkte.
  - Überlegung: Wo braucht es ComponentTests, wo UnitTests?
  
## Zusatzaufgaben III
- Die Applikation soll um eine Ausleihe erweitert werden.
- Ein Buch kann von einem Nutzer ausgeliehen und zurückgegeben werden.
  - Eine Ausleihe hat n Bücher, einen Nutzer, das Ausleihdatum und die Laufzeit.
  - Eine Ausleihe darf nicht mehr als 10 Bücher umfassen.
  - Ein Buch darf nicht ausgeliehen werden, wenn es bereits ausgeliehen ist.
  - Eine Ausleihe kann verlängert werden.
  - Ein Buch kann vor der Laufzeit zurückgegeben werden.
- Per Abfrag kann der Bestand erfasst werden:
  - Liste an ausgeliehenen Büchern
  - Liste an ausgeliehenen Büchern eines Nutzers
  - Welche Bücher werden nächste Woche | nächsten Monat zurückerwartet.
  - Welche Bücher sind überfällig und wer ist der entsprechende Nutzer?
- Entsprechende selbständige Überlegungen zur
  - API
  - dem Domain Modell
  - der Datenbank
  - der Tests

## Zusatzaufgaben IV
  - Erzeugung eines OpenAPI Spezifikation auf Grundlage der vorherrschenden API
  - Einarbeitung in die entsprechenden Annotations und dem Generator.