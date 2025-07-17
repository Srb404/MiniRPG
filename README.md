# MiniRPG

**Plugin Minecraft RPG z systemem dungeonów, NPC i progresją postaci**

![Java](https://img.shields.io/badge/Java-16+-orange)
![Minecraft](https://img.shields.io/badge/Minecraft-1.20+-green)
![Spigot](https://img.shields.io/badge/Spigot-Latest-blue)
![Status](https://img.shields.io/badge/Status-In%20Development-red)

## 📋 Opis

MiniRPG to plugin Minecraft tworzący kompletny system RPG z dungeonami, NPC, systemem statystyk i progresją postaci. Plugin oferuje zaawansowaną bazę danych MySQL do przechowywania danych graczy oraz elastyczny system ładowania contentu z plików JSON.

> ⚠️ **Uwaga**: Ten plugin jest obecnie w fazie rozwoju (in development). Funkcjonalności mogą się zmieniać, a stabilność nie jest gwarantowana.

## ✨ Główne funkcjonalności

### 🎯 System RPG
- **Poziomy i doświadczenie** - Klasyczny system progresji postaci
- **Statystyki podstawowe** - Atak, obrona, szybkość, punkty życia
- **System energii** - Ograniczenie aktywności gracza
- **Klasy postaci** - Domyślnie "Przybłęda", z możliwością rozszerzenia
- **System pieniędzy** - Waluta w grze dla ekonomii

### 🏰 System miast i reputacji
- **Poziom domu** - Progresja siedziby gracza
- **Reputacja** - System opinii w miastach
- **Dane miejskie** - Rozszerzone informacje o aktywności gracza

### 🗡️ NPC i dungeonsy
- **Dynamiczne NPC** - Ładowane z plików JSON z pełnymi statystykami
- **System dungeonów** - Wielopoziomowe lochy z bossami
- **Enemies i bosses** - Różne typy przeciwników na każdym piętrze
- **Elastyczna konfiguracja** - Łatwe dodawanie nowego contentu

### 💾 Zaawansowana baza danych
- **MySQL integration** - Profesjonalne przechowywanie danych
- **PlayerManager** - Kompleksne zarządzanie danymi graczy
- **Automatyczne tabele** - Tworzenie struktury bazy przy starcie
- **CRUD operations** - Pełne operacje tworzenia, odczytu, aktualizacji i usuwania

### 🔧 System przedmiotów
- **ItemStats** - Statystyki przedmiotów wpływające na gracza
- **Specjalne umiejętności** - System zdolności przypisanych do klas
- **ItemStack integration** - Integracja z systemem przedmiotów Minecraft

## 🚀 Instalacja

### Wymagania
- **Minecraft**: 1.20+
- **Serwer**: Spigot/Paper
- **Java**: 16+
- **Baza danych**: MySQL
- **Zależności**: Gson, Lombok, WorldEdit, Multiverse-Core

### Kroki instalacji

1. **Przygotuj bazę danych MySQL**
   ```sql
   CREATE DATABASE minirpg;
   -- Skonfiguruj użytkownika i hasło
   ```

2. **Zainstaluj zależności**
   ```bash
   # WorldEdit, Multiverse-Core w folderze plugins/
   ```

3. **Skonfiguruj połączenie z bazą**
   ```java
   // W DatabaseConnector.java
   String url = "jdbc:mysql://localhost/minirpg";
   String login = "twoj_uzytkownik";
   String password = "twoje_hasło";
   ```

4. **Przygotuj pliki danych**
   ```bash
   # Utwórz folder plugins/MiniRPG/
   # Dodaj pliki npc.json i dungeons.json
   ```

## ⚙️ Struktura danych

### Tabele bazy danych

#### PlayerData
```sql
CREATE TABLE PlayerData (
    UUID char(64) PRIMARY KEY,
    class char(32) DEFAULT 'Przybłęda',
    level int DEFAULT 1,
    exp int DEFAULT 0,
    money int DEFAULT 0,
    energy int DEFAULT 100
);
```

#### CityData
```sql
CREATE TABLE CityData (
    ID int AUTO_INCREMENT PRIMARY KEY,
    UUID varchar(64),
    homeLvl int DEFAULT 1,
    reputation int DEFAULT 0,
    FOREIGN KEY (UUID) REFERENCES PlayerData(UUID)
);
```

### Przykład NPC (npc.json)
```json
[
  {
    "id": 1,
    "type": "ZOMBIE",
    "name": "Szkielet Wojownik",
    "basicStats": {
      "attack": 15,
      "defense": 8,
      "speed": 12,
      "hp": 50
    }
  }
]
```

### Przykład Dungeonu (dungeons.json)
```json
[
  {
    "id": 1,
    "name": "Podziemna Twierdza",
    "dungeonFloors": [
      {
        "enemyNPCs": [1, 2, 3],
        "bossNPC": 4
      },
      {
        "enemyNPCs": [5, 6],
        "bossNPC": 7
      }
    ]
  }
]
```

## 🎮 API i użycie

### PlayerManager - podstawowe operacje
```java
// Tworzenie gracza
playerManager.createPlayer("uuid-gracza");

// Aktualizacja statystyk
playerManager.updatePlayerStat("uuid", PlayerDataFields.LEVEL, "10");
playerManager.updatePlayerStat("uuid", CityDataFields.HOMELVL, "3");

// Odczyt danych
String level = playerManager.getPlayerStat("uuid", PlayerDataFields.LEVEL);
String reputation = playerManager.getPlayerStat("uuid", CityDataFields.REPUTATION);

// Usuwanie gracza
playerManager.deletePlayer("uuid-gracza");
```

### StorageUtil - zarządzanie contentem
```java
// Ładowanie danych
StorageUtil.loadNPCs();
StorageUtil.loadDungeons();

// Znajdowanie obiektów
NPC npc = StorageUtil.findNPC(1);
Dungeon dungeon = StorageUtil.findDungeon(1);

// Dostęp do list
ArrayList<NPC> allNPCs = StorageUtil.getNPCs();
ArrayList<Dungeon> allDungeons = StorageUtil.getDungeons();
```

### Model danych
```java
// Podstawowe statystyki
BasicStats stats = new BasicStats(attack, defense, speed, hp);

// Dane gracza
PlayerData player = new PlayerData(level, exp, money, energy, cityData);

// NPC
NPC enemy = new NPC(id, EntityType.ZOMBIE, "Nazwa", basicStats);

// Dungeon z piętrami
DungeonFloor floor = new DungeonFloor(enemyIDs, bossID);
Dungeon dungeon = new Dungeon(id, "Nazwa", floors);
```

## 🔧 Konfiguracja

### Maven Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot-api</artifactId>
        <version>1.20.4-R0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version>
    </dependency>
</dependencies>
```

### Build Configuration
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>16</source>
                <target>16</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 📁 Struktura projektu

```
src/main/java/me/srb/minirpg/
├── MiniRPG.java              # Główna klasa pluginu
├── database/                 # System bazy danych
│   ├── DatabaseConnector.java
│   ├── PlayerManager.java
│   ├── SQLQueries.java
│   └── enums/               # Enumeracje dla tabel
├── model/                   # Modele danych
│   ├── location/           # Dungeonsy i lokacje
│   ├── npc/               # System NPC
│   ├── statistics/        # Statystyki i umiejętności
│   └── user/             # Dane graczy
└── utils/                 # Narzędzia pomocnicze
    ├── StorageUtil.java
    └── ItemStackDeserializer.java
```

## 🚧 Plany rozwoju

- **System walki** - Implementacja mechaniki combat
- **Questy** - System zadań i misji
- **Guildie** - System gildii graczy
- **Ekonomia** - Sklepy NPC i handel
- **Eventy** - Dynamiczne wydarzenia w świecie
- **Skills** - Rozszerzone drzewo umiejętności
- **Loot system** - Zaawansowany system nagród

## 🤝 Rozwój

Plugin wykorzystuje nowoczesną architekturę z separacją warstw:
- **Model** - Record classes dla immutable data
- **Database** - Dedykowana warstwa persistence
- **Utils** - Narzędzia pomocnicze i deserializacja
- **JSON Storage** - Elastyczne przechowywanie contentu

### Dodawanie nowego contentu
1. **NPC**: Dodaj do `npc.json` z odpowiednią strukturą
2. **Dungeon**: Rozszerz `dungeons.json` o nowe lokacje
3. **Statystyki**: Modyfikuj model `BasicStats` lub utwórz nowe
4. **Tabele DB**: Rozszerz enumeracje i `SQLQueries`

## 📄 Licencja

Ten projekt nie jest udostępniony na żadnej publicznej licencji. Wszystkie prawa zastrzeżone.

---

**💡 Wskazówka:** Ten plugin jest w fazie rozwoju - system RPG będzie się rozwijał wraz z dodawaniem nowych funkcjonalności. Obecnie zaimplementowano fundament z bazą danych i podstawowymi modelami danych.
