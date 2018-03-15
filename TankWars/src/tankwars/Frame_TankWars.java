package tankwars;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 *
 * @author psoderquist
 */
class Frame_TankWars extends JFrame
{
    public Arena arena;
    
    public Frame_TankWars(Arena tiles) 
    {
        this.setTitle("Tank Wars");
        
        //this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        arena = tiles;
        arena.setVisible(true);
        this.add(arena, BorderLayout.CENTER);
        this.arena.paintBoard();
        this.setSize(new Dimension(this.arena.cellWidth*this.arena.numCols + 25,this.arena.cellHeight*this.arena.numRows + 50));
        this.setVisible(true);

        //this.pack();
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
        
        addKeyListener(new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {

            Tank t = arena.getControlTank1();
            if (t != null)
            {

                if (e.getKeyCode() == KeyEvent.VK_L) {
                    t.rotateRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_K) {
                    t.rotateLeft();
                }

                if (e.getKeyCode() == KeyEvent.VK_W) {
                    t.moveUp();
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    t.moveLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    t.moveDown();
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    t.moveRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    //System.out.println("Space!");
                    t.fire();
                }
            }
            
            Tank t2 = arena.getControlTank2();
            if (t2 != null)
            {
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
                    t2.rotateRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
                    t2.rotateLeft();
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    t2.moveUp();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    t2.moveLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    t2.moveDown();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    t2.moveRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD0) {
                    //System.out.println("Space!");
                    t2.fire();
                }
            
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }
    });
    }
    
    
    
}
