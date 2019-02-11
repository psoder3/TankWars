package tankwars;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import tankwars.tanks.ControlTank;

/**
 *
 * @author psoderquist
 */
public class Arena extends JComponent {

    public int maxNumTanks = 24;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screen_width = (int)screenSize.getWidth();
    int screen_height = (int)screenSize.getHeight();
    int tankPlaces = 0;
    final int numRows = 13;
    final int numCols = 21;
    final int healthSquareSize = 16;
    final int cellHeight = (screen_height - 250) / numRows;
    final int cellWidth = (screen_width - 250) / numCols;
    private Image blankImage = null;
    private Image redSquare = null;
    private Image greenSquare = null;
    private Image lightningImg = null;
    //private int[][] grid = new int[numRows][numCols];
    private ArrayList<Tank> tanks = new ArrayList();
    private ArrayList<Bullet> bullets = new ArrayList();
    private ArrayList<Lightning> lightnings = new ArrayList();
    private ArrayList<Bomb> bombs = new ArrayList();
    private ArrayList<GameObject> tiles;
    private static boolean update = false;
    private ArrayList<TankAction> actions = new ArrayList();
    //private TimerComponent frameTimer;
    public Tank controlTank1;
    public Tank controlTank2;
    public int timeCounter = 0;
    public int timeLimit = 90;
    public int secondsLeft = timeLimit;
    private boolean gameOver = false;
    private ArrayList<Tank> resultTanks = new ArrayList();
    public ArrayList<String> tankNames;
    boolean isTeamBattle = false;
    public Frame_TankWars frame;
    private ArrayList<Integer> superTanks;
    
    /*void setFrameTimer(TimerComponent frameTimer) {
        this.frameTimer = frameTimer;
    }*/
    
    public Arena()
    {
        tiles = new ArrayList();
        superTanks = new ArrayList();
        try {
            blankImage = ImageIO.read(new File("images/sand4.png"));//blank.png"));
            redSquare = ImageIO.read(new File("images/red2.png"));
            greenSquare = ImageIO.read(new File("images/green2.png"));
            lightningImg = ImageIO.read(new File("images/lightning2.png"));
        } catch (IOException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void touchingLightning(Tank t)
    {
        for (Lightning l : lightnings)
        {
            if (l.getX() == t.getX() && l.getY() == t.getY())
            {
                if (!superTanks.contains(t.getId()))
                {
                    superTanks.add(t.getId());
                    l.destroy();
                    return;
                }
            }
        }
    }
    
    public void doAction(TankAction action)
    {
        if (action.action.equals("rotateLeft"))
        {
            action.actingTank.rotateLeftAction();
        }
        else if (action.action.equals("rotateRight"))
        {
            action.actingTank.rotateRightAction();
        }
        else if (action.action.equals("moveUp"))
        {
            action.actingTank.moveUpAction();
            touchingLightning(action.actingTank);
        }
        else if (action.action.equals("moveLeft"))
        {
            action.actingTank.moveLeftAction();
            touchingLightning(action.actingTank);
            
        }
        else if (action.action.equals("moveRight"))
        {
            action.actingTank.moveRightAction();
            touchingLightning(action.actingTank);
        }
        else if (action.action.equals("moveDown"))
        {
            action.actingTank.moveDownAction();
            touchingLightning(action.actingTank);
        }
        else if (action.action.equals("fire"))
        {
            action.actingTank.fireAction(action.actingTank);
        }
        else if (action.action.equals("setBomb"))
        {
            action.actingTank.bombAction(action.actingTank);
        }
    }
    
    public void setTanksToStartValue(int startingLives)
    {
        for (Tank t : tanks)
        {
            while (t.getLives() > startingLives) t.loseLife();
        }
    }
    
    public boolean superShootersContains(int id)
    {
        return superTanks.contains(id);
    }
    
    public Tank getControlTank1()
    {
        return controlTank1;
    }
    
    public Tank getControlTank2()
    {
        return controlTank2;
    }
    
    private void removeBulletsOffBoard() {
        List<Bullet> toRemove = new ArrayList();
        for (Bullet b : bullets)
        {
            if (gameOver)
            {
                //toRemove.add(b);
                b.destroy();
            }
            if (b.getX() < -1)
            {
                toRemove.add(b);
            }
            else if (b.getY() < -1)
            {
                toRemove.add(b);
            }
            else if (b.getX() > numCols)
            {
                toRemove.add(b);
            }
            else if (b.getY() > numRows)
            {
                toRemove.add(b);
            }
        }
        for (Bullet b : toRemove)
        {
            bullets.remove(b);
            awardBulletPoints(b, false);
        }
    }
    
    private void removeDuplicateActions() {
        List<TankAction> toRemove = new ArrayList();
        List<Tank> tankActors = new ArrayList();
        for (TankAction a : actions)
        {
            if (tankActors.contains(a.actingTank))
            {
                toRemove.add(a);
            }
            else
            {
                tankActors.add(a.actingTank);
            }
        }
        for (TankAction a : toRemove)
        {
            actions.remove(a);
        }
    }
    
    private void calculateTieBreaker() {
        int maxKills = tanks.get(0).getKills();
        int maxAlmostKills = tanks.get(0).getAlmostKills();
        double bestAccuracy = tanks.get(0).getBulletAverageAccuracy();
        Tank tMostKills = tanks.get(0);
        Tank tMostAlmostKills = tanks.get(0);
        Tank tBestAccuracy = tanks.get(0);
        for (Tank t : tanks)
        {
            if (t.equals(tanks.get(0)))
            {
                continue;
            }
            if (t.getKills() > maxKills)
            {
                maxKills = t.getKills();
                tMostKills = t;
            }
            if (t.getAlmostKills() > maxAlmostKills)
            {
                maxAlmostKills = t.getAlmostKills();
                tMostAlmostKills = t;
            }
            if (t.getBulletAverageAccuracy() < bestAccuracy)
            {
                bestAccuracy = t.getBulletAverageAccuracy();
                tBestAccuracy = t;
            }

        }
        if (isTeamBattle)
        {
            return;
        }
        int tanksWithMostKills = 0;
        for (Tank t : tanks)
        {
            if (t.getKills() == maxKills)
            {
                tanksWithMostKills += 1;
            }
        }
        if (tanksWithMostKills > 1)
        {
            int tanksWithMostAlmostKills = 0;
            for (Tank t : tanks)
            {
                if (t.getAlmostKills() == maxAlmostKills)
                {
                    tanksWithMostAlmostKills += 1;
                }
            }

            if (tanksWithMostAlmostKills > 1)
            {
                eliminateAllTanksBesides(tBestAccuracy);
            }
            else
            {
                eliminateAllTanksBesides(tMostAlmostKills);
            }
        }
        else
        {
            eliminateAllTanksBesides(tMostKills);
        }       
    }
    
    
    
    
    
    private void endGame()
    {
        
        gameOver = true;
        if (tanks.size() > 1)
        {
            calculateTieBreaker();
        }
        else
        {
            tanks.get(0).setPlaceFinished(1);
            resultTanks.add(tanks.get(0));
        }
        JTextArea textArea = new JTextArea(getResults());
        JScrollPane scrollPane = new JScrollPane(textArea);  
        textArea.setLineWrap(true);  
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        Font currentFont = textArea.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
        textArea.setFont(newFont);
        scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
        JOptionPane.showMessageDialog(this, scrollPane, "Results",  
                                       JOptionPane.PLAIN_MESSAGE);
        //JOptionPane.showMessageDialog(this, getResults());
    }
    
    private void endGameTeamBattle()
    {
        gameOver = true;
        if (tanks.size() > 1)
        {
            
            calculateTieBreaker();
        }
        else
        {
            tanks.get(0).setPlaceFinished(1);
            resultTanks.add(tanks.get(0));
        }
        JTextArea textArea = new JTextArea("TEAM RESULTS\n\n" + getTeamResults() + "\n\nINDIVIDUAL RESULTS\n\n" + getResults());
        JScrollPane scrollPane = new JScrollPane(textArea);  
        textArea.setLineWrap(true);  
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        Font currentFont = textArea.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
        textArea.setFont(newFont);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(this, scrollPane, "Results",  
                                       JOptionPane.PLAIN_MESSAGE);
        //JOptionPane.showMessageDialog(this, getResults());
    }
    
    private String getTeamResults()
    {
        HashMap<String, Integer> teamSurviveCounterMap = new HashMap();
        HashMap<String, Integer> teamKillCounterMap = new HashMap();
        
        String message = "";
        for (Tank t : resultTanks)
        {
            teamSurviveCounterMap.put(t.getTeam(), 0);
            teamKillCounterMap.put(t.getTeam(), 0);
        }
        for (Tank t : tanks)
        {
            int numberLeft = 1;
            String teamName = t.getTeam();
            if (teamSurviveCounterMap.containsKey(teamName))
            {
                int currentNumLeft = teamSurviveCounterMap.get(teamName);
                numberLeft += currentNumLeft;
            }
            teamSurviveCounterMap.put(teamName,numberLeft);
            int numberKills = t.getKills();
            if (teamKillCounterMap.containsKey(t.getTeam()))
            {
                numberKills += teamKillCounterMap.get(t.getTeam());
            }
            teamKillCounterMap.put(t.getTeam(),numberKills);
        }
        for (Tank t : resultTanks)
        {
            int numberKills = t.getKills();
            if (teamKillCounterMap.containsKey(t.getTeam()))
            {
                numberKills += teamKillCounterMap.get(t.getTeam());
            }
            teamKillCounterMap.put(t.getTeam(),numberKills);
        }
        message += "\nTANKS LEFT:\n";
        Iterator it = teamSurviveCounterMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            message += "Team " + pair.getKey() + ": " + pair.getValue() + "\n";
            it.remove(); // avoids a ConcurrentModificationException
        }
        
        message += "\nKILLS:\n";
        Iterator it2 = teamKillCounterMap.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry)it2.next();
            message += "Team " + pair.getKey() + ": " + pair.getValue() + "\n";
            it2.remove(); // avoids a ConcurrentModificationException
        }
        
        return message;
    }
    
    
    private String getResults()
    {
        String message = "";
        Collections.sort(resultTanks);
        for (int i = 0; i < resultTanks.size(); i++)
        {
            message += resultTanks.get(i).toString() + "\n";
        }
        return message;
    }
    
    public void startGame()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                final int millisecondInterval = 5;
                Timer timer = new Timer(millisecondInterval, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        removeBulletsOffBoard();
                        
                        if (gameOver)
                        {
                            return;
                        }
                        
                        if (isTeamBattle)
                        {
                            String firstTeamName = tanks.get(0).getTeam();
                            boolean teamGameOver = true;
                            for (Tank t : tanks)
                            {
                                if (!t.getTeam().equals(firstTeamName))
                                {
                                    teamGameOver = false;
                                }
                            }
                            if (teamGameOver)
                            {
                                gameOver = true;
                                removeBulletsOffBoard();
                                endGameTeamBattle();
                            }
                        }
                        else if (tanks.size() < 2)
                        {
                            gameOver = true;
                            removeBulletsOffBoard();                           
                            endGame();
                            stop();
                        }
                        
                        
                        if (actions.size() > 0)
                        {
                            removeDuplicateActions();
                            doAction(actions.get(0));
                            actions.remove(0);
                        }
                    }

                    
                });
                timer.setRepeats(true);
                timer.start();
            }
        }.start();
        
        new Thread()
        {
            @Override
            public void run()
            {
                final int millisecondInterval = 1000;
                Timer timer = new Timer(millisecondInterval, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        if (timeCounter % 3 == 0)
                        {
                            addLightning();
                            //numberTanksAtBeginning = tanks.size();
                        }
                        if (gameOver)
                        {
                            return;
                        }
                        timeCounter++;
                        int secondsExpired = timeCounter;
                        secondsLeft = timeLimit - secondsExpired;
                        if (secondsLeft < 1)
                        {
                            secondsLeft = 0;
                            gameOver = true;
                            removeBulletsOffBoard();
                            if (isTeamBattle)
                            {
                                                                  
                                endGameTeamBattle();
                            }
                            else
                            {
                                endGame();
                            }
                        }
                        frame.timerLabel.setText("    " + secondsLeft);
                        //System.out.println("SECONDS LEFT: " + secondsLeft);
                        
                        
                    }

                    
                });
                timer.setRepeats(true);
                timer.start();
            }
        }.start();
    }
    
    private void addLightning()
    {
        int lx = 0;
        int ly = 0;
        boolean spotTaken;
        do
        {
            spotTaken = false;
            lx = (int) (Math.random() * this.numCols);
            ly = (int) (Math.random() * this.numRows);
            for (Tank t : tanks)
            {
                if (t.getX() == lx && t.getY() == ly) 
                {
                    spotTaken = true;
                    break;
                }
            }
            if (spotTaken) continue;
            for (Lightning l : lightnings)
            {
                if (l.getX() == lx && l.getY() == ly)
                {
                    spotTaken = true;
                    break;
                }
            }
           
        } while (spotTaken);
        final Lightning l = new Lightning(lx,ly);
        
        
        lightnings.add(l);
        
        int lightningInterval = 1000;
        
        Timer timer = new Timer(lightningInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (l == null || !l.isAlive())
                {
                    return;
                }
                l.tickClock();
                if (!l.isAlive()) lightnings.remove(l);

                paintBoard();
            }
        });
        timer.setRepeats(true); // Only execute once
        timer.start();
    }
    
    public List<Tank> getUnmodifiableTanks() {
        List<Tank> clone_tanks = new ArrayList();
        for(Tank t : tanks) {
            clone_tanks.add(t.getCopyTank());
        }
        return clone_tanks;
    }
    
    public List<Bullet> getUnmodifiableBullets() {
        List<Bullet> clone_bullets = new ArrayList();
        for(Bullet b : bullets) {
            clone_bullets.add(b.getCopy());
        }
        return clone_bullets;
    }
    
    public List<Lightning> getUnmodifiableLightnings() {
        List<Lightning> clone_lightnings = new ArrayList();
        for(Lightning l : lightnings) {
            clone_lightnings.add(l.getCopy());
        }
        return clone_lightnings;
    }
    
    public List<Bomb> getUnmodifiableBombs() {
        List<Bomb> clone_bombs = new ArrayList();
        for(Bomb b : bombs) {
            clone_bombs.add(b.getCopy());
        }
        return clone_bombs;
    }
    
    public void addTank(Tank tank) {
        if (tankPlaces >= maxNumTanks)
        {
            return;
        }
        if (tank.getClass() == ControlTank.class)
        {
            if (controlTank1 == null)
            {
                controlTank1 = tank;
            }
            else
            {
                controlTank2 = tank;
            }
        }
        tanks.add(tank);
        tankPlaces++;
        //grid[(int)tank.getY()][(int)tank.getX()] = 1;
    }

    
    /*
    public void addPlaceHolder() {
        if (tankPlaces > 23)
        {
            return;
        }
        if (tanks.size() == 23)
        {
            controlTank1 = tanks.get(4);
            controlTank2 = tanks.get(22);
        }
        tankPlaces ++;
    }
    */
    
    void addAction(TankAction tankAction) {
        actions.add(tankAction);
    }
    
    void recalculateGrid()
    {
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {
                //grid[i][j] = 0;
            }
        }
        for (Tank tank : tanks)
        {
            //grid[(int)tank.getY()][(int)tank.getX()] = 1;
        }
    }
    
    
    void paintBoard() {
        //recalculateGrid();
        tiles.clear();
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {
                //if (grid[i][j] != 1)
                {
                    int x = j * cellWidth;
                    int y = i * cellHeight;
                    drawCell(blankImage,x,y,new GameObject());
                }
            }
        }
        for (Tank tank : tanks)
        {
            if (tank.isAlive())
            {
                drawObject(tank, true);
            }
        }
        for (Bullet bullet : bullets)
        {
            if (bullet.isAlive())
            {
                drawObject(bullet, false);
            }
        }
        for (Lightning lightning : lightnings)
        {
            if (lightning.isAlive())
            {
                drawObject(lightning, false);
            }
        }
        for (Bomb bomb : bombs)
        {
            if (bomb.isAlive())
            {
                drawObject(bomb, false);
            }
        }
        this.repaint();
    }

    private void drawObject(GameObject gameObject, boolean isTank) {
        drawCell(gameObject.image, gameObject.getX() * cellWidth, gameObject.getY() * cellHeight, gameObject);
        if ((isTank || frame.colorBullets.isSelected()) && isTeamBattle && frame.colorTeams.isSelected())
        {
            drawCell(gameObject.team_image, gameObject.getX() * cellWidth, gameObject.getY() * cellHeight, new GameObject());
        }
        if (isTank && frame.livesCheckBox.isSelected())
        {
            Tank t = (Tank)gameObject;
            int maxLives = (int)frame.numLivesBox.getSelectedItem();
            for (int i = 0; i < maxLives; i++)
            {
                Image square;
                if (i >= t.getLives())
                {
                    square = redSquare;
                }
                else
                {
                    square = greenSquare;
                }
                drawCell(square, gameObject.getX() * cellWidth+i*healthSquareSize, gameObject.getY() * cellHeight, new GameObject());
            }
        }
    }

    private void drawCell(Image img, double x, double y, GameObject t) {

        t.drawingImage = new DrawingImage(img, new Rectangle2D.Double(x, y, cellWidth, cellHeight));
        tiles.add(t);
    }
    
    boolean checkCollision(Bullet b)
    {
        
        Tank owner = null;
        for (Tank t : tanks)
        {
            if (b.isBulletsOwner(t))
            {
                owner = t;
                break;
            }
        }
        
        
        for (Bomb bomb : bombs)
        {
            if (b.getX() == bomb.getX() && b.getY() == bomb.getY())
            {
                b.destroy();
                bullets.remove(b);
                explode(bomb);
                return true;
            }
        }
        
        
        for (Tank tank : tanks)
        {
            if (b.getX() == tank.getX() && b.getY() == tank.getY())
            {
                if ((frame.canKillTeammates.isSelected() == false && tank.getTeam().equals(b.getTeam())) || tank.equals(owner))
                {
                    continue;
                }
                //tanks.remove(tank);
                //grid[(int)tank.getY()][(int)tank.getX()] = 0;
                tank.loseLife();
                if (tank.getLives() <= 0)
                {
                    tank.destroy();
                    tank.setPlaceFinished(tanks.size());
                    tanks.remove(tank);
                    resultTanks.add(tank);                
                }
                awardBulletPoints(b, true);
                b.destroy();
                bullets.remove(b);
                //System.out.println(tank.toString());


                if (tanks.size() == 1)
                {
                    //System.out.println(tanks.get(0).toString());
                }
                return true;
            }
        }
        if (frame.bulletsCollide.isSelected())
        {
            for (Bullet bullet : bullets)
            {
                if (b.getX() == bullet.getX() && b.getY() == bullet.getY())
                {
                    if (b.equals(bullet))
                    {
                        continue;
                    }
                    bullets.remove(bullet);
                    bullets.remove(b);

                    awardBulletPoints(bullet, false);
                    awardBulletPoints(bullet, false);

                    b.destroy();
                    //grid[(int)bullet.y][(int)bullet.x] = 0;
                    bullet.destroy();
                    return true;
                }
            }
        }
        return false;
    }
    
    void fireBullet(int rotateDegrees, double x, double y, Tank tank) {
        final Bullet b = new Bullet(rotateDegrees, x, y, tank);
        
        bullets.add(b);
        
        int bulletInterval = 125;
        if (superTanks.contains(tank.getId()))
        {
            bulletInterval = 63;
        }
        
        Timer timer = new Timer(bulletInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!b.isAlive())
                {
                    return;
                }
                if (b.getRotateDegrees() == 0)
                {
                    b.setBulletX(b.getX()+.5);
                }
                if (b.getRotateDegrees() == 180)
                {
                    b.setBulletX(b.getX()-.5);
                }
                if (b.getRotateDegrees() == 90)
                {
                    b.setBulletY(b.getY()+.5);
                }
                if (b.getRotateDegrees() == 270)
                {
                    b.setBulletY(b.getY()-.5);
                }
                checkBulletDistanceToATank(b);
                checkCollision(b);
                paintBoard();
            }
        });
        timer.setRepeats(true); // Only execute once
        timer.start();
    }
    
    
    public void setBomb(double x, double y, Tank t)
    {
        final Bomb b = new Bomb(x, y, t);
        for (Bomb b2 : bombs)
        {
            if (b2.getX() == x && b2.getY() == y)
            {
                return;
            }
        }
        bombs.add(b);
        
        int bombInterval = 1000;
        
        
        Timer timer = new Timer(bombInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!b.isAlive())
                {
                    return;
                }
                b.tickClock();
                if (!b.isAlive()) 
                {
                    explode(b);
                }
                paintBoard();
            }
        });
        timer.setRepeats(true); // Only execute once
        timer.start();
    }
    
    private void explode(Bomb b)
    {
        int blastRadius = 3;
        ArrayList<Tank> tanksToDie = new ArrayList();
        for (Tank t : tanks)
        {
            if (Math.abs(t.getX() - b.getX()) < blastRadius
                    &&
                    Math.abs(t.getY() - b.getY()) < blastRadius)
            {
                tanksToDie.add(t);
            }
        }
        
        for (Tank t : tanksToDie)
        {
            t.destroy();
            tanks.remove(t);
            b.awardBombKill(true);
        }
        b.destroy();
        bombs.remove(b);
                
        for (Bomb b2 : bombs)
        {
            if (Math.abs(b2.getX() - b.getX()) < blastRadius
                    &&
                    Math.abs(b2.getY() - b.getY()) < blastRadius)
            {
                explode(b2);
            }
        }
    }
    
    private void awardBulletPoints(Bullet b, boolean killed)
    {
        b.awardBulletPoints(killed);    
    }
    
    private double distanceFromBulletToTank(Bullet b, Tank t)
    {
        double yoffset = b.getY() - t.getY();
        double xoffset = b.getX() - t.getX();
        
        double distance = Math.sqrt(xoffset*xoffset + yoffset*yoffset);
        return distance; 
    }
    
    // used to determine a winner when no tank outlasts the others
    private void checkBulletDistanceToATank(Bullet b)
    {
        for (Tank t : tanks)
        {
            if (b.isBulletsOwner(t) || (isTeamBattle && b.getTeam().equals(t.getTeam())))
            {
                continue;
            }
            double distance = distanceFromBulletToTank(b,t);
            // if bullet distance to a tank is closer than closest already, set this as min
            if (distance < b.getMinDistance())
            {
                b.setMinDistance(distance);
            }
            // if bullet distance to a tank it is facing is closer than closest already, set this as min for that
            if (distance < b.getMinFacingDistance())
            {
                if (t.getX() < b.getX() && b.getRotateDegrees() == 180 && t.getY() == b.getY())
                {
                    b.setMinFacingDistance(distance);
                }
                if (t.getX() > b.getX() && b.getRotateDegrees() == 0 && t.getY() == b.getY())
                {
                    b.setMinFacingDistance(distance);
                }
                if (t.getX() == b.getX() && b.getRotateDegrees() == 270 && t.getY() < b.getY())
                {
                    b.setMinFacingDistance(distance);
                }
                if (t.getX() == b.getX() && b.getRotateDegrees() == 90 && t.getY() > b.getY())
                {                    
                    b.setMinFacingDistance(distance);                
                }
            }
        }
        
        
        
    }
    
    private void eliminateAllTanksBesides(Tank winningTank) {
        for (Tank t : tanks)
        {
            t.setPlaceFinished(tanks.size());
            resultTanks.add(t);
        }
        tanks = new ArrayList();
        winningTank.setPlaceFinished(1);
        tanks.add(winningTank);
    }	
	
	
	
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        drawBackground(g2);
        drawShapes(g2);
    }


    private void drawBackground(Graphics2D g2) {
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
    }


    private void drawShapes(Graphics2D g2) {

        
        for (GameObject O : tiles) {
            DrawingShape shape = O.drawingImage;
            AffineTransform at = new AffineTransform();
            
            AffineTransform oldXForm = g2.getTransform();
            at.rotate(Math.toRadians(O.getRotateDegrees()), (cellWidth+2)/2 + O.getX()*cellWidth, (cellHeight+2)/2 + O.getY()*cellHeight);
            g2.transform(at);
            shape.draw(g2);
            g2.setTransform(oldXForm);
        }
    }

    

    

    

       

    

    

    
    
    
    
    
    
    
    interface DrawingShape {
	boolean contains(Graphics2D g2, double x, double y);
	void adjustPosition(double dx, double dy);
	void draw(Graphics2D g2);
    }




    class DrawingImage implements DrawingShape {

	public Image image;
	public Rectangle2D rect;
        public int value;
	
	public DrawingImage(Image image, Rectangle2D rect) {
		this.image = image;
		this.rect = rect;
                //this.value = value;
	}

	@Override
	public boolean contains(Graphics2D g2, double x, double y) {
		return rect.contains(x, y);
	}

	@Override
	public void adjustPosition(double dx, double dy) {
		rect.setRect(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());	
	}

	@Override
	public void draw(Graphics2D g2) {
		Rectangle2D bounds = rect.getBounds2D();
                if (image == null)
                {
                    System.out.println("Pause here");
                }
                if (image.getHeight(null) == healthSquareSize)
                {
                    g2.drawImage(image, (int)bounds.getMinX(), (int)bounds.getMinY()-(int)((1.0/8)*cellWidth), (int)bounds.getMaxX()-(int)((7.0/8)*cellWidth), (int)bounds.getMaxY()-(int)((7.0/8)*cellWidth),
                    0, 0, image.getWidth(null), image.getHeight(null), null);
                }
                else
                {
                    g2.drawImage(image, (int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getMaxX(), (int)bounds.getMaxY(),
                    0, 0, image.getWidth(null), image.getHeight(null), null);
                }
	}	
    }
}