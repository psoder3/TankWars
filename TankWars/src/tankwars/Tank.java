package tankwars;

import tankwars.tanks.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author psoderquist
 */
public class Tank extends GameObject implements Comparable<Tank>{
    
    public Tank(){}
    
    public void tankAction(){
        
    }
    
    private int kills = 0;
    private int almostKills = 0;
    private double bulletAverageAccuracy = Double.MAX_VALUE;
    private double totalShots = 0;
    private int placeFinished = 0;
    
    public Tank(String filename, Arena a)
    {
        super("tank", a);
        image = loadImage(filename);
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (gameHasStarted())
                {
                    tankAction();
                }
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    public String toString()
    {
        return "Tank: " + this.getClass()
                + "\nplace finished: " + placeFinished
                + "\nkills: " + kills 
                + "\nalmost kills: " + almostKills 
                + "\nbullet average error: " + bulletAverageAccuracy 
                + "\ntotal shots: " + totalShots + "\n";
    }
    
    public void setPlaceFinished(int place)
    {
        placeFinished = place;
    }
    
    
    @Override
    public int compareTo(Tank o)
    {
        Tank t = (Tank)o;
        if (t.placeFinished == this.placeFinished)
        {
            return 0;
        }
        else if (t.placeFinished < this.placeFinished)
        {
            return 1;
        }
        return -1;
    }
    
    void awardBulletPoints(Bullet b, boolean killed) {
        
        double numerator = (totalShots * bulletAverageAccuracy) + b.getMinDistance();
        totalShots += 1;
        // update tank's bullet average
        bulletAverageAccuracy = numerator / totalShots;
        // if minDistance == 0 increase kills by 1
        if (killed)
        {
            kills += 1;
        }
        // else if facingdistance min is < 3, increase almost kills by 1
        else if (b.getMinFacingDistance() < 3)
        {
            almostKills += 1;
        }
    }
    
    void awardBombKill(Bomb b) {
        kills += 1;
    }

    public int getAlmostKills()
    {
        return almostKills;
    }
    
    public int getKills()
    {
        return kills;
    }
    
    public double getBulletAverageAccuracy()
    {
        return bulletAverageAccuracy;
    }
    
    public double getTotalShots()
    {
        return totalShots;
    }
    
    
    
}
