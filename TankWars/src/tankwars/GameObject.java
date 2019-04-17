package tankwars;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import tankwars.Arena.DrawingImage;

/**
 *
 * @author psoderquist
 */
public class GameObject {
    private int id;
    public static int current_id = 0;
    private int numTankRows = 6;
    static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    public Image image = null;
    private double x = 0;
    private double y = 0;
    String filename;
    public DrawingImage drawingImage;
    public Image team_image;
    private int rotateDegrees = 0;
    private boolean canMakeAction = true;
    private Arena arena;
    private boolean alive = true;
    private String team_name;
    public static ArrayList<String> teamNames;
    private int lives = 5;

    static {
        teamNames = new ArrayList();
    }
    
    // super constructor for a bullet to call
    public GameObject(int rotationDegrees, double x, double y, Tank tank)
    {
        this.x = x;
        this.y = y;
        this.rotateDegrees = rotationDegrees;
        this.id = current_id++;
    }
    
    public int getNumCols()
    {
        return arena.numCols;
    }
    
    public int getNumRows()
    {
        return arena.numRows;
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public String getTeam()
    {
        return team_name;
    }
    
    public void setTeam(String team_name)
    {
        this.team_name = team_name;
        try {
            String filename = "images/team_colors/" + (GameObject.teamNames.indexOf("team " + team_name)+1) + ".png";
            team_image = ImageIO.read(new File(filename));
        } catch (IOException ex) {
            //Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Bullet> getBullets()
    {
        return arena.getUnmodifiableBullets();
    }

    public List<Tank> getTanks()
    {
        return arena.getUnmodifiableTanks();
    }
    
    public List<Lightning> getLightnings()
    {
        return arena.getUnmodifiableLightnings();
    }
    
    public List<Heart> getHearts()
    {
        return arena.getUnmodifiableHearts();
    }
    
    public List<Bomb> getBombs()
    {
        return arena.getUnmodifiableBombs();
    }
    
    public boolean isSuperShooter()
    {
        return arena.superShootersContains(id);
    }
    
    public int getRotateDegrees ()
    {
        return rotateDegrees;
    }
    
    public GameObject(DrawingImage DI, Arena arena)
    {
        drawingImage = DI;
        this.arena = arena;
        this.id = current_id++;
    }
    
    // super constructor for tanks to call
    public GameObject(String type, Arena a)
    {
        arena = a;
        this.id = current_id++;
        if (type.equals("tank"))
        {                
            int numTanks = a.tankPlaces;

            if (a.tankNames.size() == 2)
            {
                if (numTanks == 0)
                {
                    x = 6;
                    y = 6;
                }
                else if (numTanks == 1)
                {
                    x = 14;
                    y = 6;
                }
            }
            else if (a.tankNames.size() == 4)
            {
                if (numTanks == 0)
                {
                    x = 5;
                    y = 3;
                }
                else if (numTanks == 1)
                {
                    x = 15;
                    y = 3;
                }
                else if (numTanks == 2)
                {
                    x = 5;
                    y = 9;
                }
                else if (numTanks == 3)
                {
                    x = 15;
                    y = 9;
                }
            }
            else if (a.tankNames.size() == 8)
            {
                if (numTanks == 0)
                {
                    x = 4;
                    y = 0;
                }
                else if (numTanks == 1)
                {
                    x = 16;
                    y = 0;
                }
                else if (numTanks == 2)
                {
                    x = 4;
                    y = 12;
                }
                else if (numTanks == 3)
                {
                    x = 16;
                    y = 12;
                }
                else if (numTanks == 4)
                {
                    x = 8;
                    y = 4;
                }
                else if (numTanks == 5)
                {
                    x = 12;
                    y = 4;
                }
                else if (numTanks == 6)
                {
                    x = 8;
                    y = 8;
                }
                else if (numTanks == 7)
                {
                    x = 12;
                    y = 8;
                }
            }
            else
            {
                x = (numTanks%numTankRows)*4;
                y = (numTanks/numTankRows)*4;
            }
            
        }
    }
    
    public GameObject()
    {
        this.id = current_id++;
    }
    
    public boolean isAlive()
    {
        return alive;
    }
    
    public int getId()
    {
        return id;
    }
    
    protected Image loadImage(String imageFile) 
    {
        try 
        {
            return ImageIO.read(new File(imageFile));
        }
        catch (IOException e) {
                return NULL_IMAGE;
        }
    }
    
    public boolean rotateRightAction(double key)
    {
        if (key != arena.randPassword)
        {
            return false;
        }
        if (!canMakeAction || !alive)
        {
            return false;
        }
        rotateDegrees += 90;
        if (rotateDegrees >= 360)
        {
            rotateDegrees -= 360;
        }
        //Arena.update = true;
        arena.repaint();
        return true;
    }
    
    public void rotateRight()
    {
        arena.addAction(new TankAction("rotateRight",(Tank)this));
    }

    public boolean rotateLeftAction(double key)
    {
        if (key != arena.randPassword)
        {
            return false;
        }
        if (!canMakeAction || !alive)
        {
            return false;
        }
        rotateDegrees -= 90;
        if (rotateDegrees < 0)
        {
            rotateDegrees += 360;
        }
        arena.repaint();
        //Arena.update = true;
        return true;
    }
    
    public void rotateLeft()
    {
        arena.addAction(new TankAction("rotateLeft",(Tank)this));
    }
    
    public boolean moveUpAction(double key)
    {
        if (key != arena.randPassword)
        {
            return false;
        }
        boolean spotTaken = false;
        for (Tank t : getTanks())
        {
            if (t.getX() == x && t.getY() == y - 1)
            {
                spotTaken = true;
                break;
            }
        }
        if (!canMakeAction || !alive || spotTaken)
        {
            return false;
        }
        if (y > 0)
        {
            y--;
        }
        else
        {
            return false;
        }
        arena.paintBoard();
        return true;
    }
    
    public void moveUp()
    {
        arena.addAction(new TankAction("moveUp",(Tank)this));
    }
    
    public boolean moveLeftAction(double key)
    {
        if (key != arena.randPassword)
        {
            return false;
        }
        boolean spotTaken = false;
        for (Tank t : getTanks())
        {
            if (t.getX() == x-1 && t.getY() == y)
            {
                spotTaken = true;
                break;
            }
        }
        if (!canMakeAction || !alive || spotTaken)
        {
            return false;
        }
        if (x > 0)
        {
            x--;
        }
        arena.paintBoard();
        return true;
    }
    
    public void moveLeft()
    {
        arena.addAction(new TankAction("moveLeft",(Tank)this));
    }
    
    public boolean moveDownAction(double key)
    {
        if (key != arena.randPassword)
        {
            return false;
        }
        boolean spotTaken = false;
        for (Tank t : getTanks())
        {
            if (t.getX() == x && t.getY() == y + 1)
            {
                spotTaken = true;
                break;
            }
        }
        if (!canMakeAction || !alive || spotTaken)
        {
            return false;
        }
        if (y < arena.numRows-1)
        {
            y++;
        }
        arena.paintBoard();
        return true;
    }
    
    public void moveDown()
    {
        arena.addAction(new TankAction("moveDown",(Tank)this));
    }
    
    public boolean moveRightAction(double key)
    {
        if (key != arena.randPassword)
        {
            return false;
        }
        boolean spotTaken = false;
        for (Tank t : getTanks())
        {
            if (t.getX() == x+1 && t.getY() == y)
            {
                spotTaken = true;
                break;
            }
        }
        if (!canMakeAction || !alive || spotTaken)
        {
            return false;
        }
        if (x < arena.numCols-1)
        {
            x++;
        }
        arena.paintBoard();
        return true;
    }
    
    public void moveRight()
    {
        arena.addAction(new TankAction("moveRight",(Tank)this));
    }
    
    public boolean fireAction(Tank tank, double key)
    {
        if (key != arena.randPassword)
        {
            return false;
        }
        if (!canMakeAction || !alive)
        {
            return false;
        }
        arena.fireBullet(rotateDegrees, x, y, tank);
        arena.paintBoard();
        return true;
    }
    
    public void fire()
    {
        arena.addAction(new TankAction("fire",(Tank)this));
    }
    
    public boolean bombAction(Tank tank, double key)
    {
        if (key != arena.randPassword)
        {
            return false;
        }
        if (!canMakeAction || !alive)
        {
            return false;
        }
        arena.setBomb(x, y, tank);
        arena.paintBoard();
        return true;
    }
    
    // Bombs take away two lives if you're within 2 squares of them
    // It costs the take that lays the bomb 1 life though
    public void setBomb()
    {
        arena.addAction(new TankAction("setBomb",(Tank)this));
    }
    
    public void addLife(double key)
    {
        if (key == arena.randPassword)
        {
            this.lives++;
        }
    }
    
    public void loseLife()
    {
        
        this.lives--;
        
    }
    
    public int getLives()
    {
        return lives;
    }
    
    public void destroy()
    {
        alive = false;
    }
    
    public Tank getCopyTank()
    {
        GameObject new_tank = new Tank();
        new_tank.x = this.x;
        new_tank.y = this.y;
        new_tank.id = this.id;
        new_tank.rotateDegrees = this.rotateDegrees;
        new_tank.team_name = this.team_name;
        return (Tank)new_tank;
    }
    
}
