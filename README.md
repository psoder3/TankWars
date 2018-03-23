# AITankWars

## Project Description

Participants will create a class that inherits from Tank class and can do anything they want in the overridden method tankAction(). It gets executed every half-second. If you were to edit the constructor you could get around that so that's off limits. Other than that I believe it's pretty bulletproof.

## Objective

Be the last tank standing. If there are multiple tanks left over after time expires the tie breaker scenario is as follows:

* 1st Tie Breaker: Most kills
* 2nd Tie Breaker: Most "almost kill"s (shooting within 3 spaces of killing a tank, but it dodges)
* 3rd Tie Breaker: Best average bullet accuracy (each bullet keeps track of the closest it gets to any other tank)

## Here are the possible moves 
(if you try to do more than one of these in the tankAction() method only the first one will work):
 
```
public void rotateRight()

public void rotateLeft()

public void moveUp()

public void moveDown()

public void moveRight()

public void moveLeft()

public void fire()
```

if any of the above fail (maybe because somebody was in the place your tank tried to move, you lose that move. You can avoid that by checking the position before moving, etc.)
 

## And here are some read-only's that will help you make the move decision each time:
```
// returns the number of Columns in the arena
public int getNumCols()    

// returns the number of Rows in the arena
public int getNumRows()
 
// returns x position [0-20] of an object (both bullets and tanks)
public double getX()  

// returns y position [0-12] of an object (both bullets and tanks)
public double getY()

// returns a list of all active bullets in the arena (in read-only form)
public List<Bullet> getBullets()

// returns a list of all active tanks in the arena (in read-only form)
public List<Tank> getTanks()
    
// returns the rotation degrees of an object (both bullets and tanks)
// 0 is facing right, 90 is down, 180 is left, 270 is up
// (a bullet's rotation tells you what direction it is moving)
public int getRotateDegrees ()
 
// returns true if this bullet was fired by the given tank
// (useful for not worrying about your own bullets)
boolean isBulletsOwner(Tank tank) 
 
// returns the name of the team this player is on
// if not a team battle, a tank's team is its ordinal number of being created
String getTeam()

```
 
*Important: The image file you create to represent your tank MUST be the same name and a .png file. For example, my Tank is in PaulTank.java and the image is PaulTank.png - The name PaulTank is also used to place my tank in the arena and must be placed in the text file that is being read in as the battle arrangement. See the arrangements folder to know what I mean.

## Following is the DumbTank AI - It just chooses a random move every time.
You may use it as a starting point for your own tank.
___________________________________________________________

```
package tankwars.tanks;

import tankwars.*;

/**
 *
 * @author psoderquist
 */
public class DumbTank extends Tank {

    public DumbTank(String filename, Arena a) {
        super(filename, a);
    }
            
    @Override
    public void tankAction() {
        
        int rand = (int)(Math.random() * 10);
        if (rand == 0)
        {
            moveLeft();
        }
        else if (rand == 1)
        {
            moveRight();
        }
        else if (rand == 2)
        {
            moveUp();
        }
        else if (rand == 3)
        {
            moveDown();
        }
        else if (rand == 4)
        {
            rotateRight();
        }
        else if (rand == 5)
        {
            rotateLeft();
        }
        else if (rand == 6)
        {
            fire();
        }
        
    }
    
}

```

## Here is a demo video:
https://youtu.be/HcBJ-RTERqM

## Here are some videos to help you get started:
https://www.youtube.com/watch?v=dIWPcICkJHY

https://www.youtube.com/watch?v=5JbqSgMCo78

https://www.youtube.com/watch?v=UvMEr2K-eII

## Controllable Tanks (for Testing Purposes):
I've programmed a special type of tank that can be controlled (for purposes of testing another tank's response to its actions). To use this tank include a ControlTank in the arrangement you're using. To move up,left,down,right use keys W,A,S,D. To rotate right or left use l and k. To shoot use space. If you wish to use two control tanks the second one will be controlled with The direction arrows, 5,6 to rotate right and left, 0 to fire.

## Team Battles:
If you wish to do team battles, simply type something such as "Team Awesome" in the arrangement file you're using on the line above all the tanks you wish to be on that team. As long as the first word is "Team" it will add all following tanks to that team until another team is specified. Then you can use the function getTeam() on any tank to know what team it belongs to.
