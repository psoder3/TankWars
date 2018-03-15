package tankwars;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import tankwars.Arena.DrawingImage;

/**
 *
 * @author psoderquist
 */
public class GameObject {
    static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    public Image image = null;
    private double x = 0;
    private double y = 0;
    String filename;
    public DrawingImage drawingImage;
    private int rotateDegrees = 0;
    private boolean canMakeAction = true;
    private Arena arena;
    private boolean alive = true;
    
    // super constructor for a bullet to call
    public GameObject(int rotationDegrees, double x, double y, Tank tank)
    {
        this.x = x;
        this.y = y;
        this.rotateDegrees = rotationDegrees;
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
    
    public List<Bullet> getBullets()
    {
        return arena.getUnmodifiableBullets();
    }

    public List<Tank> getTanks()
    {
        return arena.getUnmodifiableTanks();
    }
    
    public int getRotateDegrees ()
    {
        return rotateDegrees;
    }
    
    public GameObject(DrawingImage DI, Arena arena)
    {
        drawingImage = DI;
        this.arena = arena;
    }
    
    // super constructor for tanks to call
    public GameObject(String type, Arena a)
    {
        arena = a;
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
            else
            {
                x = (numTanks%6)*4;
                y = (numTanks/6)*4;
            }
            
        }
    }
    
    public GameObject()
    {
        
    }
    
    public boolean isAlive()
    {
        return alive;
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
    
    public boolean rotateRightAction()
    {
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

    public boolean rotateLeftAction()
    {
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
    
    public boolean moveUpAction()
    {
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
    
    public boolean moveLeftAction()
    {
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
    
    public boolean moveDownAction()
    {
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
    
    public boolean moveRightAction()
    {
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
    
    public boolean fireAction(Tank tank)
    {
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
    
    public void destroy()
    {
        alive = false;
    }
}
