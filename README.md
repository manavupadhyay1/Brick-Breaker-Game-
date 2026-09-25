# 🧱 Brick Breaker

A Java-based Brick Breaker game where the player controls a paddle to bounce a ball and destroy bricks. The game combines classic arcade gameplay with Java Object-Oriented Programming concepts, providing an interactive and challenging experience.

## 🎮 Game Overview

The objective of the game is simple: control the paddle, keep the ball from falling, and break as many bricks as possible.

The player earns points by destroying bricks and must carefully control the paddle to keep the ball in play. As the game progresses, the player can face increasingly challenging gameplay.

## ✨ Features

* 🎮 Interactive paddle controls
* 🧱 Breakable brick system
* ⚪ Real-time ball movement
* 💥 Ball and brick collision detection
* 🏓 Paddle and ball collision detection
* ⭐ Score tracking
* ❤️ Life/game-over system
* 🏆 Win condition
* 🔄 Restart functionality
* 🖥️ Java graphical interface

## 🛠️ Technologies Used

* **Java**
* **Java Swing**
* **Java AWT**
* Object-Oriented Programming
* Event Handling
* Collision Detection
* Game Loop
* 2D Graphics

## 🧠 OOP Concepts Used

The project applies important Object-Oriented Programming concepts to structure the game.

### Classes & Objects

Different components of the game are represented using classes and objects.

### Encapsulation

Game properties and behaviours are organized within their respective classes.

### Inheritance

Common functionality can be reused through inheritance between related game components.

### Polymorphism

Different objects can implement or respond to common behaviours in different ways.

### Abstraction

Complex game operations are separated into manageable components to keep the code organized.

## 🎯 How to Play

1. Start the game.
2. Use the paddle controls to move left and right.
3. Bounce the ball using the paddle.
4. Destroy the bricks using the ball.
5. Avoid letting the ball fall below the paddle.
6. Continue until all bricks are destroyed.
7. Try to achieve the highest possible score.

## 🕹️ Controls

| Key   | Action                |
| ----- | --------------------- |
| ←     | Move paddle left      |
| →     | Move paddle right     |
| Space | Start / continue game |
| R     | Restart game          |

> Controls may vary depending on the implementation of the current version.

## ⚙️ Game Mechanics

### Ball Movement

The ball continuously moves across the game area and changes direction when it collides with objects or boundaries.

### Collision Detection

The game detects collisions between:

* Ball and paddle
* Ball and bricks
* Ball and game boundaries

### Brick Destruction

When the ball hits a brick, the brick is removed and the player's score increases.

### Score System

Players receive points for successfully destroying bricks.

### Game Over

The game ends when the player loses all available attempts or the ball falls below the paddle.

### Victory

The player wins after successfully destroying all the bricks.

## ▶️ How to Run

### Prerequisites

Install **Java JDK** on your system.

Check the installation:

```bash
java --version
```

and:

```bash
javac --version
```

### Compile

Navigate to the project directory and compile the Java source files.

For a project using a `src` folder:

```bash
javac -d out src/*.java
```

### Run

```bash
java -cp out Main
```

If your main class belongs to a package, use the appropriate fully qualified class name.

## 📂 Project Structure

```text
Brick-Breaker/
│
├── src/
│   ├── Main.java
│   ├── GamePanel.java
│   ├── Paddle.java
│   ├── Ball.java
│   ├── Brick.java
│   └── ...
│
├── out/
├── README.md
└── .gitignore
```

The exact class structure may vary depending on the implementation.

## 🚀 Future Improvements

Possible enhancements include:

* Multiple difficulty levels
* Different brick types
* Power-ups
* Multiple levels
* Sound effects
* Background music
* Improved animations
* High-score leaderboard
* Increasing ball speed
* Special bonus bricks
* Improved graphics and themes

## 📚 Learning Outcomes

This project helped demonstrate the practical use of:

* Java programming
* Object-Oriented Programming
* GUI development
* Event-driven programming
* Collision detection
* Game loops
* Keyboard input handling
* Basic 2D game development

## 👨‍💻 Project

**Brick Breaker Game**

A Java-based academic project developed to apply Object-Oriented Programming concepts through an interactive 2D arcade game.
