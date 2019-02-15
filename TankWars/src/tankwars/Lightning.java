package tankwars;

public class Lightning extends GameObject {
    private int timeLeft = 20;
    private double lightning_x = 0;
    private double lightning_y = 0;
    private String filename2 = "images/lightningSmall.png";
    
    public Lightning(double x, double y)
    {
        super(0, x, y, null);
        lightning_x = x;
        lightning_y = y;
        filename = "images/lightningBig.png";
        image = loadImage(filename);
    }
    public int getTimeLeft()
    {
        return timeLeft;
    }
    public void tickClock()
    {
        if (timeLeft % 2 == 0)
        {
            image = loadImage(filename2);
        }
        else
        {
            image = loadImage(filename);
        }
        timeLeft--;
        if (timeLeft < 1)
        {
            this.destroy();
        }
    }
    
    @Override
    public double getX()
    {
        return lightning_x;
    }
 
    @Override
    public double getY()
    {
        return lightning_y;
    }
    
    public Lightning getCopy()
    {
        Lightning new_lightning = new Lightning(this.lightning_x,this.lightning_y);
        new_lightning.timeLeft = this.timeLeft;
        new_lightning.lightning_x = this.lightning_x;
        new_lightning.lightning_y = this.lightning_y;
        return new_lightning;
    }
}
