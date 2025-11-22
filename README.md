# Java 2D Roguelike

Ein klassisches 2D Roguelike-Spiel entwickelt in Java mit Swing.

## Features

- **Prozedural generierte Dungeons**: Jede Spielrunde bietet eine einzigartige Dungeon-Erfahrung
- **Rundenbasiertes Gameplay**: Taktisches, rundenbasiertes Kampfsystem
- **Charakter-Progression**: Sammle Erfahrung, steige Level auf und werde stärker
- **Field of View (FOV)**: Realistisches Sichtfeld-System mit Fog of War
- **Inventarsystem**: Sammle und benutze Items wie Heiltränke, Waffen und Rüstungen
- **Verschiedene Gegner**: Kämpfe gegen Goblins, Orks und Trolle
- **Mehrere Dungeon-Ebenen**: Steige tiefer in den Dungeon hinab für größere Herausforderungen
- **Permadeath**: Der klassische Roguelike-Modus - wenn du stirbst, ist das Spiel vorbei!

## Anforderungen

- Java 11 oder höher
- Maven (zum Bauen des Projekts)

## Installation und Start

### Mit Maven:

```bash
# Projekt kompilieren
mvn clean compile

# Spiel ausführen
mvn exec:java -Dexec.mainClass="com.roguelike.Main"
```

### JAR-Datei erstellen:

```bash
# JAR erstellen
mvn clean package

# JAR ausführen
java -jar target/java-roguelike-1.0-SNAPSHOT.jar
```

## Steuerung

### Bewegung
- **W** - Nach oben bewegen
- **A** - Nach links bewegen
- **S** - Nach unten bewegen
- **D** - Nach rechts bewegen
- **Pfeiltasten** - Alternative Bewegungssteuerung (↑ ↓ ← →)

### Aktionen
- **E** - Item aufheben
- **R** - Treppe hinabsteigen
- **H** - Hilfe anzeigen
- **1-9** - Item aus Inventar benutzen (Nummer entspricht Position im Inventar)
- **ENTER** - Neues Spiel starten (im Hauptmenü)
- **R** - Neues Spiel starten (im Game Over Bildschirm)

## Gameplay

### Ziel
Erkunde den prozedural generierten Dungeon, besiege Monster, sammle Items und steige so tief wie möglich hinab!

### Kampfsystem
- Bewege dich auf ein Monster zu, um es anzugreifen
- Schaden wird basierend auf Angriff und Verteidigung berechnet
- Monster greifen dich an, wenn sie dich sehen können
- Besiege Monster, um Erfahrungspunkte zu erhalten

### Items
- **Heiltränke (!)**: Stellen Trefferpunkte wieder her (sofortige Wirkung)
- **Schwerter (/)**: Erhöhen deinen Angriffswert (+3 ATK wenn ausgerüstet)
- **Schilde ([)**: Erhöhen deine Verteidigung (+2 DEF wenn ausgerüstet)

### Equipment-System
- Sammle Waffen und Rüstungen im Dungeon
- Benutze eine Waffe oder Rüstung aus deinem Inventar (Taste 1-9), um sie auszurüsten
- Wenn du bereits ein Item desselben Typs ausgerüstet hast, wird es automatisch gegen das neue ausgetauscht
- Das alte Equipment wird zurück ins Inventar gelegt
- Deine aktuellen Stats werden sofort aktualisiert und in der Sidebar angezeigt

### Level-System
- Sammle Erfahrungspunkte durch das Besiegen von Monstern
- Steige Level auf, um stärker zu werden
- Jeder Level-Aufstieg erhöht HP, Angriff und Verteidigung

### Field of View
- Du kannst nur sehen, was in deinem Sichtfeld liegt
- Bereiche, die du bereits erkundet hast, werden dunkel dargestellt
- Monster können nur angreifen, wenn sie dich sehen können

## Projektstruktur

```
src/main/java/com/roguelike/
├── Main.java                  # Einstiegspunkt
├── engine/
│   ├── Game.java             # Hauptspiel-Logik und Game Loop
│   ├── GameState.java        # Spiel-Zustände
│   └── Position.java         # 2D Position Helper
├── entity/
│   ├── Entity.java           # Basis-Klasse für alle Entities
│   ├── Player.java           # Spieler-Charakter
│   └── Enemy.java            # Gegner (Goblin, Orc, Troll)
├── world/
│   ├── Tile.java             # Dungeon-Tiles (Wand, Boden, etc.)
│   ├── Room.java             # Raum-Struktur
│   ├── DungeonGenerator.java # Prozeduraler Dungeon-Generator
│   └── GameMap.java          # Spielwelt mit Tiles und Entities
├── item/
│   ├── Item.java             # Item-Klasse
│   └── Inventory.java        # Inventar-System
├── system/
│   ├── CombatSystem.java     # Kampf-Mechanik
│   ├── FOVSystem.java        # Field of View Berechnung
│   └── MessageLog.java       # Nachrichten-System
└── ui/
    ├── GamePanel.java        # Haupt-Rendering Panel
    └── GameFrame.java        # Fenster und Input-Handling
```

## Technische Details

### Technologien
- **Java 11**: Programmiersprache
- **Maven**: Build-Management
- **Swing**: GUI-Framework
- **Bresenham-Algorithmus**: Line-of-Sight Berechnung für FOV

### Spielmechaniken
- **Dungeon-Generierung**: BSP-ähnlicher Raum- und Korridor-Generator
- **FOV-System**: Raycast-basierte Sichtfeld-Berechnung
- **Pathfinding**: Einfache direkte Verfolgung für Monster-AI
- **Combat**: Angriff vs. Verteidigung mit Zufallsvarianz
- **Equipment**: Vollständiges Ausrüstungssystem mit Waffen und Rüstungen

## Mögliche Erweiterungen

- Magiesystem mit Sprüchen
- Mehr Monster-Typen mit verschiedenen AI-Verhaltensweisen
- Verbessertes Pathfinding (A* Algorithmus)
- Speicher- und Ladefunktion
- Mehr Item-Typen (Scrolls, Wands, Ringe, etc.)
- Boss-Monster
- Hunger-System
- Quests

## Lizenz

Dieses Projekt wurde als Lernprojekt erstellt und ist frei verfügbar.

## Credits

Entwickelt als klassisches Roguelike im Stil von Rogue, NetHack und anderen Genre-Klassikern.
