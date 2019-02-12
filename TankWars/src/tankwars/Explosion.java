package tankwars;

public class Explosion extends GameObject {
    private int timeLeft = 1;
    private double explosion_x = 0;
    private double explosion_y = 0;
    public Explosion(double x, double y)
    {
        super(0, x, y, null);
        filename = "images/explosion.png";
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
}
