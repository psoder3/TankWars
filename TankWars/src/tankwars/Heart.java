package tankwars;

public class Heart extends GameObject {
    private int timeLeft = 20;
    private double heart_x = 0;
    private double heart_y = 0;
    String filename2 = "images/greenHeartSmall.png";
    public Heart(double x, double y)
    {
        super(0, x, y, null);
        heart_x = x;
        heart_y = y;
        filename = "images/greenHeart.png";
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
        return heart_x;
    }
 
    @Override
    public double getY()
    {
        return heart_y;
    }
    
    public Heart getCopy()
    {
        Heart new_heart = new Heart(this.heart_x,this.heart_y);
        new_heart.timeLeft = this.timeLeft;
        new_heart.heart_x = this.heart_x;
        new_heart.heart_y = this.heart_y;
        return new_heart;
    }
}
