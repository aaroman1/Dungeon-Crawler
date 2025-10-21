# Dungeon Crawler (JavaFX)

A small MVC-structured dungeon crawler built with JavaFX. The app renders a grid-based board with a hero, enemies, walls, treasure, and an exit. Game state lives in the **model**, user input is handled by the **controller**, and all rendering happens in the **view**. An **Observer/Subject** pattern keeps the UI in sync with the model.

> Works locally without network. Drop it into your Java workspace and run.

---

## ✨ Features

* **Clean MVC**: `Model` (game state/logic), `View` (JavaFX UI), `Controller` (user input → model actions)
* **Observer pattern**: Views subscribe to the model and re-render on updates
* **Grid board** with typed pieces: `Hero`, `Enemy`, `Treasure`, `Wall`, `Exit`
* **Simple controls**: arrow keys and on-screen buttons (if enabled)
* **Score/level** tracking (current + high score)
* **Pluggable assets** via `resourcePath` on pieces

---

## 🧭 Project Structure

```
src/main/java/
└── com/comp301/a08dungeon/
    ├── Main.java
    ├── controller/
    │   ├── Controller.java
    │   └── ControllerImpl.java
    ├── model/
    │   ├── Model.java
    │   ├── ModelImpl.java
    │   ├── Subject.java
    │   ├── Observer.java
    │   ├── board/
    │   │   ├── Board.java
    │   │   ├── BoardImpl.java
    │   │   └── Posn.java
    │   └── pieces/
    │       ├── APiece.java
    │       ├── MovablePiece.java
    │       ├── CollisionResult.java
    │       ├── Piece.java
    │       ├── Hero.java
    │       ├── Enemy.java
    │       ├── Treasure.java
    │       ├── Wall.java
    │       └── Exit.java
    └── view/
        ├── FXComponent.java
        ├── View.java
        ├── AppLauncher.java
        ├── GameView.java
        └── TitleScreenView.java
```

---

## 🧩 Architecture

**MVC + Observer**

* **Model** — owns game state (board, pieces, scores, status) and rules (movement, collisions).
* **View** — renders UI (title screen + game board HUD) and implements `Observer`.
* **Controller** — translates input to `Model` calls (`moveUp/Down/Left/Right()`, `startGame()`).

The model calls `notifyObservers()` after every state change; the view’s `update()` re-renders.

**Key types**

* `Board`/`BoardImpl`: 2D grid of `Piece`s addressed by `Posn(row,col)`
* `Piece` hierarchy:

  * `APiece` (base: name, position, resource path)
  * `MovablePiece` (adds `collide()` returning `CollisionResult`)
  * Concrete pieces: `Hero`, `Enemy`, `Treasure`, `Wall`, `Exit`

---

## 🖥️ Running the App

### Prereqs

* **Java 17+** (recommended)
* **JavaFX 17+** available (via Gradle/Maven dependency or local SDK)

### Option A — Gradle (recommended)

Add JavaFX to your build:

```gradle
plugins {
  id 'application'
}

application {
  mainClass = 'com.comp301.a08dungeon.Main' // or your launcher class
}

dependencies {
  implementation "org.openjfx:javafx-controls:22"
  implementation "org.openjfx:javafx-graphics:22"
}
```

Run:

```bash
./gradlew run
```

### Option B — Maven

```xml
<dependencies>
  <dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>22</version>
  </dependency>
  <dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-graphics</artifactId>
    <version>22</version>
  </dependency>
</dependencies>
```

Run:

```bash
mvn javafx:run
```

### Option C — Plain `java` (local JavaFX SDK)

If you installed a JavaFX SDK locally:

```bash
# Compile
find src/main/java -name "*.java" | xargs javac

# Run (replace with your JavaFX SDK path)
java --module-path /path/to/javafx/lib \
     --add-modules javafx.controls,javafx.graphics \
     com.comp301.a08dungeon.Main
```

> Replace `/path/to/javafx/lib` with your JavaFX SDK path.

---

## 🎮 Controls

* **Start**: Click **Start Game** on the title screen
* **Move**: Arrow keys (↑ ↓ ← →)
* **On-screen buttons**: If shown in `GameView`, click arrows to move

---

## 🧠 Game Rules (high-level)

* Move the **Hero** to collect **Treasure** and reach the **Exit**
* **Walls** block movement
* **Enemies** may end the run or affect score/health (see `collide()` logic)
* Scoring/leveling defined in `ModelImpl`

*(Exact collision outcomes live in `Hero.collide()` / `Enemy.collide()` and `CollisionResult`.)*

---

## 🖼️ Assets

Pieces can expose a `resourcePath` (e.g., `images/hero.png`).
Place images in `src/main/resources/images/` and (optionally) swap text glyphs for `ImageView`s in `GameView`.

---

## ✅ Testing & Debug Tips

* Ensure **every** mutating model method calls `notifyObservers()`
* If keyboard input dies after scene swaps, request focus on the root node
* Log moves/collisions in `ModelImpl` while debugging
* Confirm `BoardImpl` bounds and `Posn` equality/`hashCode()`

---

## 📦 Packaging

* **Gradle**: `./gradlew distZip` (or use the Application plugin)
* **Maven**: Shade plugin for a fat JAR
* **Native**: `jlink`/`jpackage` with JavaFX modules for platform-specific bundles

---

## 🗺️ Roadmap Ideas

* Animated movement & tweening
* Enemy AI (patrol/chase)
* Multiple dungeon layouts / procedural generation
* Health/lives or power-ups
* Sprite-based rendering + sound effects

---

## 🔒 License

MIT. See `LICENSE` in this repo.

---

## 🙌 Acknowledgements

Built for a JavaFX + MVC assignment. Thanks to the JavaFX community for tooling and examples.

---

## 📷 Screenshots (optional)

```
docs/
 ├─ title.png
 └─ game.png
```

Embed in README:

```md
![Title Screen](docs/title.png)
![Game View](docs/game.png)
```

---

### Quick Start (TL;DR)

```bash
# Java 17+ and JavaFX available
./gradlew run
# or
mvn javafx:run
# or
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.graphics com.comp301.a08dungeon.Main
```

Use arrow keys to move. Collect treasure. Find the exit. Have fun 🗝️🧱🧙‍♂️.
