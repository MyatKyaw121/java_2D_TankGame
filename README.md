# java_2D_TankGame
Java 2D tank game that includes destructible walls, 2 AI tanks and certain boosters, as well as some UI such as tank health.


Detail description.

This program focuses on MVC (model-view-control) concept. Animations are implemented by using Java Swing.

It features, single player tank alongside 2 AI tanks. 

Chasing AI tank
that follows playerTank if the distance between them is less than 300 but once the player got offRadar ( which is powerUp), then it stops following Player

SmartAI tank
the other AI tank is it calculates player's directions and shoots at player.

Player tank 
moves by means of keyboard registration. ( keyboard reader is used)

Power up - That enables tanks to get 2 times faster speed (and also OffRadar effect for Player which stops the ChasingAI tank from following it )

Animations- Explosions were added (Shell explosions-when shell hits the objects, Big explosions- when things were destroyed )

Game UI- GameScreen shows the health, PowerUps of playerTank, AI tanks.

UP ARROW    --> move up
DOWN ARROW  --> move down
LEFT ARROW  --> move left
RIGHT ARROW --> move right 
ESCAPE KEY  --> game is ended

each tank, walls have health. Tanks have 3 health, walls have 4.

Collision detection works as intended. (for example, if 2 shells hit each other, both will be destoryed)




