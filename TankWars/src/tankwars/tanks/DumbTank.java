package tankwars.tanks;

import tankwars.*;

/**
 *
 * @author psoderquist
 */
public class DumbTank extends Tank {

    public DumbTank(String filename, Arena a) {
        super(filename, a);
    }
            
    @Override
    public void tankAction() {
        
        int rand = (int)(Math.random() * 100);
        if (rand % 10 == 0)
        {
            moveLeft();
        }
        else if (rand % 10 == 1)
        {
            moveRight();
        }
        else if (rand % 10 == 2)
        {
            moveUp();
        }
        else if (rand % 10 == 3)
        {
            moveDown();
        }
        else if (rand % 10 == 4)
        {
            rotateRight();
        }
        else if (rand % 10 == 5)
        {
            rotateLeft();
        }
        else if (rand % 10 == 6)
        {
            fire();
        }
        else if (rand % 10 == 7)
        {
            setBomb();
        }

    }  
    
}
