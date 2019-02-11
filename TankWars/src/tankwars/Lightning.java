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
public class Lightning extends GameObject {
    private int timeLeft = 10;
    private double lightning_x = 0;
    private double lightning_y = 0;
    public Lightning(double x, double y)
    {
        super(0, x, y, null);
        filename = "images/lightning2.png";
        image = loadImage(filename);
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
    
    public Lightning getCopy()
    {
        Lightning new_lightning = new Lightning(this.lightning_x,this.lightning_y);
        new_lightning.timeLeft = this.timeLeft;
        new_lightning.lightning_x = this.getX();
        new_lightning.lightning_y = this.getY();
        return new_lightning;
    }
}
