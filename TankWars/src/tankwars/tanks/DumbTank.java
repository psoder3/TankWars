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
        
        int rand = (int)(Math.random() * 10);
        if (rand == 0)
        {
            moveLeft();
        }
        else if (rand == 1)
        {
            moveRight();
        }
        else if (rand == 2)
        {
            moveUp();
        }
        else if (rand == 3)
        {
            moveDown();
        }
        else if (rand == 4)
        {
            rotateRight();
        }
        else if (rand == 5)
        {
            rotateLeft();
        }
        else if (rand == 6)
        {
            fire();
        }

    }  
    
}
