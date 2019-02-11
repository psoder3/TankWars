package tankwars;

/**
 *
 * @author psoderquist
 */
public class Bullet extends GameObject {
    
    private Tank sourceTank;

    private double bulletX;
    private double bulletY;
    private int bulletRotateDegrees;
    
    // give it a high starting value
    private double minFacingDistance = Double.MAX_VALUE;
    private double minDistance = Double.MAX_VALUE;
    
    public Bullet(int rotateDegrees, double x, double y, Tank tank){
        super(rotateDegrees, x, y, tank);
        filename = "images/bullets/bullet2.png";
        image = loadImage(filename);
        sourceTank = tank;
        bulletX = x;
        bulletY = y;
        bulletRotateDegrees = rotateDegrees;
        setTeam(tank.getTeam());
    }
    
    public Bullet()
    {
        
    }
    
    
    public void awardBulletPoints(boolean killed)
    {
        sourceTank.awardBulletPoints(this, killed);
    }
    
    public Bullet(String filename)
    {
        image = loadImage(filename);
    }

    public void setBulletX(double x) {
        bulletX = x;
    }

    public void setBulletY(double y) {
        bulletY = y;
    }
    
    public double getMinFacingDistance()
    {
        return minFacingDistance;
    }
    
    public double getMinDistance()
    {
        return minDistance;
    }
    
    public void setMinFacingDistance(double d)
    {
        minFacingDistance = d;
    }
    
    public void setMinDistance(double d)
    {
        minDistance = d;
    }
    
    @Override
    public double getX()
    {
        return bulletX;
    }
 
    @Override
    public double getY()
    {
        return bulletY;
    }
    
    @Override
    public int getRotateDegrees()
    {
        return bulletRotateDegrees;
    }

    public boolean isBulletsOwner(Tank tank) {
        return tank.equals(sourceTank);
    }
    
    public Bullet getCopy()
    {
        Bullet new_bullet = new Bullet();
        new_bullet.sourceTank = this.sourceTank;
        new_bullet.bulletX = this.bulletX;
        new_bullet.bulletY = this.bulletY;
        new_bullet.bulletRotateDegrees = this.getRotateDegrees();
        return new_bullet;
    }
}