/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankwars;

/**
 *
 * @author pablosoderquist
 */
public class Bomb extends GameObject {
    private int timeLeft = 6;
    private double bomb_x = 0;
    private double bomb_y = 0;
    private Tank sourceTank;
    private String filename1 = "images/bomb1.png";
    private String filename2 = "images/bomb2.png";
    private String filename3 = "images/bomb3.png";
    
    public Bomb(double x, double y, Tank t)
    {
        super(0, x, y, null);
        bomb_x = x;
        bomb_y = y;
        filename = "images/bomb.png";
        image = loadImage(filename);
        this.sourceTank = t;
    }
    
    public void awardBombKill(boolean killed)
    {
        sourceTank.awardBombKill(this);
    }
    
    public int getTimeLeft()
    {
        return timeLeft;
    }
    
    @Override
    public double getX()
    {
        return bomb_x;
    }
 
    @Override
    public double getY()
    {
        return bomb_y;
    }
    
    public void tickClock()
    {
        if (timeLeft % 2 == 1)
        {
            image = loadImage(filename);
        }
        else if (timeLeft == 2)
        {
            image = loadImage(filename1);
        }
        else if (timeLeft == 4)
        {
            image = loadImage(filename2);
        }
        else if (timeLeft == 6)
        {
            image = loadImage(filename3);
        }
        if (timeLeft < 1)
        {
            this.destroy();
        }
        timeLeft--;
    }
    
    public Bomb getCopy()
    {
        Bomb new_bomb = new Bomb(this.bomb_x,this.bomb_y, sourceTank);
        new_bomb.timeLeft = this.timeLeft;
        new_bomb.bomb_x = this.bomb_x;
        new_bomb.bomb_y = this.bomb_y;
        return new_bomb;
    }
}
