# RoboRally - DTU 02324 Advanced Programming course

## Playing the game

To play the game, you'd first need a RoboRally executable JAR-file.
If you don't already have one, you can either download it, or compile it yourself using one of the two provided methods below.

When you have the RoboRally JAR-file, you simply need to double-click it to start playing!
If you don't have the Java Runtime assigned to open JAR-files, please either assign it yourself, or run it with `java -jar roborally-x.x.x.jar` in the console.
Have fun!

### Downloading RoboRally (precompiled)

You can download RoboRally from the [Releases tab](https://github.com/DTUSoftware/RoboRally-F22/releases) on the RoboRally GitHub!
(There might not currently be any available release, as RoboRally is still in the development phase.)

### Compiling RoboRally

To compile RoboRally, you need to have Java 17 and Maven installed.

Instructions:
1. Open a command prompt in the RoboRally root directory (where the README is located).
2. Type `mvn` to run the default goal (creating executable and JavaDoc) - if you just want the executable, use `mvn clean package`.
3. The compiled JAR-file can now be found in the `target` folder in the project directory - for example `roborally-1.0.0.jar`.

## Current Features

- Checkpoints (collect them all and win the game!)
- Playing different maps
- Saving and loading the game
- Walls
- Conveyor Belts
- Pits
- Reboot tokens
- Spawn gears
- Rebooting ("bad cards" aren't implemented yet)
- Robots
- Gears
- Pushing robots
- Push panels (they push, but they don't check registers at the moment)
- A lot of programming cards (not all, not all special either)
- Phases
- Programming and activating robots
- A lot of visual elements from the board game

## Taxonomy

### Objects (nouns)

#### Board / Game
The game board, consisting of fields for the Player to place their robots on.

#### Robot / Player
The Player is usually represented by their 'robot' on the board.

#### Turn
A player turn.

#### Round
A collection of turns (when every player is done, the round ends).

#### Command
A command used to program a robot with.

#### Command Card
A card with a command associated with it.

#### Hand Card
A random selection of Command Cards, that the user
can use to program their robot with.

#### Program
A set of instructions (command cards) that have been
assigned to a robot.

#### Field / Space
A field on the board, that can have multiple objects associated with it,
like robots, lasers, walls, rollers, etc.

#### (Priority) Antenna
The antenna is used to decide who has priority (who gets to start first).

#### Energy Cube
Used to purchase upgrade cards for the robots.

#### Conveyor Belt
Active at end of register, moves robot or turns robot

#### Push Panel
Robot ends register number on push panel with given number gets pushed

#### Gear
Starting positions for robots, and turns around on each round.

#### Board Laser
End of register shoots robot in their path

#### Pit
Fall off the map, reboot

#### Energy Space
Grab energy cube, ends on fifth register also gets one.

#### Wall
Blocks laser and robots 

#### Checkpoint
Use to win the game

#### Racing Course
A collection of Checkpoints, ended with a Goal.

#### Damage
SPAM, TROJAN HORSE, WORM, VIRUS

### Actions (verbs)

#### Begin Game
Begins the game.

#### Win the game
First player to make way over checkpoints in numerical order

#### Move
Move forward, turn right, turn left, etc...

#### Activate Program / Robot
Performs the program that the player programmed the robot to do.

#### Program Robots
Assign Hand Cards to the Robot's Program.

#### Rebooting
Two spam cards, stop current program, place robot on reboot token

#### Damage
Do card action, then cards action from deck on same register