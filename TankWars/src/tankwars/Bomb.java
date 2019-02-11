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
    private int timeLeft = 3;
    private double bomb_x = 0;
    private double bomb_y = 0;
    private Tank sourceTank;
    public Bomb(double x, double y, Tank t)
    {
        super(0, x, y, null);
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
    public void tickClock()
    {
        timeLeft--;
        if (timeLeft < 1)
        {
            this.destroy();
        }
    }
    
    public Bomb getCopy()
    {
        Bomb new_bomb = new Bomb(this.bomb_x,this.bomb_y, sourceTank);
        new_bomb.timeLeft = this.timeLeft;
        new_bomb.bomb_x = this.getX();
        new_bomb.bomb_y = this.getY();
        return new_bomb;
    }
}
