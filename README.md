# BlackJack-Trainer

### Made By: Alex Li and Yifan Zong 

## Documentation
[Link to JavaDoc API](https://1liale.github.io/BlackJack-Trainer/)

## General info:
This project is designed to help players learn the basic strategy and card counting strategy in Blackjack.
Can also be used to simulate the risk of ruins of various betting spreads.
Uses the "standard" casino rules.
More specifically: Hit on hard 17 (H17), 3:2 blackjack payout, Double-Down-after-Split (DDAS), Resplit Aces (RAS).
Does not allow surrender.

## How to play:
[link to official rules](https://www.blackjack.org/blackjack-rules/)

First choose the number of decks in the playing shoe, the deck penetration, and the number of players in the game.

There are 3 different selectable players:
### 1. Human player: allows you to control all betting and playing decisions.

  (to help players learn, we have incorporated a hint function which allows player to request for the optimal 
  strategy/play at each step)
### 2. Basic Strategy Player: CPU plays exactly according to the basic strategy chart.

![alt text](https://www.blackjackapprenticeship.com/wp-content/uploads/2018/08/BJA_Basic_Strategy.jpg)
  
### 3. Card Counting Player: The ultimate player
Use card counting to play according to the basic strat and the illustrious 18 deviations.
Bet according to a user specified betting spread.
Note that card counting player does not quit even with a true count below D1.
(incorporating both Basic Strat and Card Counting strategies to decide best course of action as well as betting units)
[Click to learn more about card counting](https://youtu.be/dQw4w9WgXcQ)


## Project Specifications:
- Project is created using Java 
- At the present, this program only supports CLI.
	
## Setup
```
option 1:

$ download as zip folder
$ run in ide such as Eclipse or IntelliJ   

option 2:
$ git clone https://github.com/1liale/BlackJack-Trainer.git
$ open terminal and go to repo
$ java -jar "BlackJackTrainer.jar"

```
