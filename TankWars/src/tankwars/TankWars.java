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
    //TimerComponent frameTimer;
    Frame_TankWars frameTankWars;
    public static boolean randomArrangement = true;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            TankWars tw = new TankWars();
            
            //String filename = "control_vs_control.txt";
            //String filename = "paul_vs_dumb.txt";
            //String filename = "arr5.txt";
            //String filename = "24_paul_tanks.txt";
            //String filename = "multi_teams.txt";
            //String filename = "four_tanks.txt";
            String filename = "AllTanks.txt";
            File inputFile = new File("arrangements/" + filename);
            Scanner file_reader = new Scanner(inputFile);
            tw.arena.tankNames = new ArrayList();
            String current_team = "";
            
            while(file_reader.hasNextLine())
            {
                String name = file_reader.nextLine();
                if (name.substring(0, 5).toLowerCase().equals("team "))
                {
                    tw.arena.isTeamBattle = true;
                    if (!GameObject.teamNames.contains(name.toLowerCase()))
                    {
                        GameObject.teamNames.add(name.toLowerCase());
                    }
                    current_team = name.toLowerCase();
                }
                else
                {
                    String suffix = "";
                    if (!current_team.equals(""))
                    {
                        suffix = current_team.substring(5);
                        tw.arena.tankNames.add(name + " " + suffix);

                    }
                    else
                    {
                        tw.arena.tankNames.add(name + " " + tw.arena.tankNames.size());                    
                    } 
                       
                }
            }
            
            if (randomArrangement)
            {
                shuffleTanks(tw.arena.tankNames);
            }
            for (String name : tw.arena.tankNames)
            {
                Scanner strScan = new Scanner(name);
                String class_name = strScan.next();
                String team_name = strScan.next();
                String tank_filename = "images" + File.separator + "tanks" 
                        + File.separator + class_name + ".png";
                Arena arena_reference = tw.arena;
                Class cl;
                try {
                    cl = Class.forName("tankwars.tanks." + class_name);
                    Constructor con;

                    con = cl.getConstructor(String.class, Arena.class);

                    Object o = con.newInstance(tank_filename, arena_reference);            
                    Tank tank = (Tank)o;
                    tank.setTeam(team_name);
                    tw.arena.addTank(tank);
                    
                } catch (Exception ex) {
                    Logger.getLogger(TankWars.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
            tw.frameTankWars = new Frame_TankWars(tw.arena);
            
            //tw.arena.setFrameTimer(tw.frameTimer);
            //tw.arena.startGame();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TankWars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}