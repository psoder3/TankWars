package tankwars;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.*;


/**
 *
 * @author psoderquist
 */
public class TankWars {

    private static void shuffleTanks(ArrayList<String> tankNames) {
        Collections.shuffle(tankNames);
    }
    
    Arena arena = new Arena();
    Frame_Timer frameTimer;
    Frame_TankWars frameTankWars;
    public static boolean randomArrangement = true;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            TankWars tw = new TankWars();
            
            //String filename = "paul_vs_dumb.txt";
            String filename = "arr5.txt";
            //String filename = "24_paul_tanks.txt";
            //String filename = "paul_tank_23_dumb_tanks.txt";
            //String filename = "four_tanks.txt";
            File inputFile = new File("arrangements/" + filename);
            Scanner file_reader = new Scanner(inputFile);
            tw.arena.tankNames = new ArrayList();
            while(file_reader.hasNext())
            {
                String name = file_reader.next();
                tw.arena.tankNames.add(name);
            }
            
            if (randomArrangement)
            {
                shuffleTanks(tw.arena.tankNames);
            }
            for (String name : tw.arena.tankNames)
            {
                String tank_filename = "images" + File.separator + "tanks" 
                        + File.separator + name + ".png";
                Arena arena_reference = tw.arena;
                Class cl;
                try {
                    cl = Class.forName("tankwars.tanks." + name);
                    Constructor con;

                    con = cl.getConstructor(String.class, Arena.class);

                    Object tank = con.newInstance(tank_filename, arena_reference);                    
                    tw.arena.addTank((Tank)tank);

                } catch (Exception ex) {
                    Logger.getLogger(TankWars.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
            tw.frameTankWars = new Frame_TankWars(tw.arena);
            tw.frameTimer = new Frame_Timer(tw.arena);
            tw.arena.setFrameTimer(tw.frameTimer);
            tw.arena.startGame();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TankWars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
